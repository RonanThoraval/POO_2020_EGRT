/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteExplosion;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.view.sprite.SpriteMonster;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> spritesDecor = new ArrayList<>();
    private List<SpriteMonster> spritesMonster=new ArrayList<>();
    private List<SpriteBomb> spritesBomb=new ArrayList<>();
    private List<SpriteExplosion> spritesExplosion=new ArrayList<>();
    
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        

        game.getMonsters().forEach(go -> spritesMonster.add(SpriteFactory.createMonster(layer, go)));
        game.getWorld().forEach( (pos,d) -> spritesDecor.add(SpriteFactory.createDecor(layer, pos, d)));
		game.getExplosion().get(game.getCurrentLevel()).forEach(e -> spritesExplosion.add(SpriteFactory.createExplosion(layer,e)));
        player.getListBombs().get(game.getCurrentLevel()).forEach(b-> spritesBomb.add(SpriteFactory.createBomb(layer, b)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        

    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                try {
					update(now);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isKey()) {
        	player.requestOpenDoor();
        }
        if (input.isBomb()) {
        	player.requestBomb();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }
    
    
    /**
     * 
     * @param p
     * @param level
     * @return true if a monster is at the position "p" in level "level"
     */
    private boolean isMonsterHere(Position p,int level) {
    	for (Monster monster : game.getMonsters(level)) {
    		if (monster.getPosition().equals(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param bombPosition, the position of the bomb
     * @param position, a position where the explosion can potentially go
     * @param level
     * @return true if something is between the bomb's position and the position past in parameters, false else
     * If something is between the bomb and the object, the object can not be affected.
     */
    private boolean isBehindSomething(Position bombPosition, Position position,int level) {
    	if (Math.abs(bombPosition.x-position.x)<=1 && Math.abs(bombPosition.y-position.y)<=1) {
    		return false;
    	}
    	
    	if(position.x==bombPosition.x) {
    		if(position.y>bombPosition.y) {
    			for(int i=1; i<position.y-bombPosition.y; i++) {
    				Position p = new Position(bombPosition.x,bombPosition.y+i);
    				if( !game.getWorld(level).isEmpty(p) || isMonsterHere(p,level) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}else {
    			for(int i=1; i<bombPosition.y-position.y; i++) {
    				Position p = new Position(bombPosition.x,bombPosition.y-i);
    				if( !game.getWorld(level).isEmpty(p) || isMonsterHere(p,level) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}
    	}else {
    		if(position.x>bombPosition.x) {
    			for(int i=1; i<position.x-bombPosition.x; i++) {
    				Position p = new Position(bombPosition.x+i,bombPosition.y);
    				if( !game.getWorld(level).isEmpty(p) || isMonsterHere(p,level) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}else {
    			for(int i=1; i<bombPosition.x-position.x; i++) {
    				Position p = new Position(bombPosition.x-i,bombPosition.y);
    				if( !game.getWorld(level).isEmpty(p) || isMonsterHere(p,level) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }

    /**
     * 
     * @param bombPosition, the position of the bomb
     * @param positionsAround, a list of potential positions of explosions
     * @param level, the level where the actualization takes places
     * @param now, the moment when/where the actualization takes places
     * @return a list of positions where decor must be deleted
     * 
     * This function also kills enemies and decrease player's lives when it is necessary
     */
    private List<Position> bombDamage(Position bombPosition, List<Position> positionsAround, int level,long now) {
    	List<Position> positionToSupp=new ArrayList<>();
    	Iterator<Position> iterator=positionsAround.iterator();
    	while (iterator.hasNext()) {
    		Position next=iterator.next();
    		
    		if (!isBehindSomething(bombPosition,next,level) && (game.getWorld(level).isEmpty(next) || game.getWorld(level).get(next).canExplose())) {
    			Iterator<Monster> iteratorMonster=game.getMonsters(level).iterator();
        		while (iteratorMonster.hasNext()) {
        			Monster monster=iteratorMonster.next();
        			if (monster.getPosition().equals(next) && !(isBehindSomething(bombPosition,next,level))) {
        				monster.setDeath();
        			}
        		}
    			if (player.getPosition().equals(next)) {
        			player.decreaseLives(now);
        		}
    			positionToSupp.add(next);		
    		}
    	}
    	
    	Iterator<Monster> iteratorMonster=game.getMonsters(level).iterator();
		while (iteratorMonster.hasNext()) {
			Monster monster=iteratorMonster.next();
			if (!monster.isAlive()) {
				iteratorMonster.remove();
			}
		}
    	
    	Iterator<Position> iterator2=positionToSupp.iterator();
    	while(iterator2.hasNext()) {
    		game.getWorld(level).clear(iterator2.next());
    	}
    	return positionToSupp;
    }
    
    
    
    private void update(long now) throws IOException {
    	// case 1 : new level
    	if (game.hasChangedLevel()) {
    		spritesDecor.clear();
    		spritesMonster.clear();
    		spritesBomb.clear();
    		spritesExplosion.clear();
    		
    		initialize(stage,game);
    		game.setChangedLevel(false);	
    	}
    	
    	//case 2 : Decor has been modified ( box has been moved, bonus has been taken )
    	if (game.getWorld().hasChanged()) {
    		spritesDecor.forEach(s -> s.remove());
    		spritesDecor.clear();
    		
    		game.getWorld().forEach( (pos,d) -> spritesDecor.add(SpriteFactory.createDecor(layer, pos, d)));
    		game.getWorld().setChanged(false);
    	}
    	
        player.update(now);
        
        
        //update state of bombs and explosions of all levels, creates sprites of new bombs and explosions
        for (int i=0 ; i<game.getNbLevels(); i++) {
        	Iterator<Bomb> iter=player.getListBombs().get(i).iterator();
        	while (iter.hasNext()) {
	        	Bomb bomb=iter.next();
	        	if (bomb.hasBeenCreated()) {
	        		if (!bomb.exploded()) {
	        			//the bomb hasn't exploded yet, updates the sprite
	            		bomb.update(now);
	        		}else {
	        			//the bomb has exploded, we have to create the explosions around
	    	       		player.increaseNbBombs();
	    	       		//Search of the affected positions
	        			List<Position> positionsAround=bomb.positionsAroundBomb(bomb.getRange(),i);
	        			//Search of the affected objects
	        			List<Position> positionToSupp = bombDamage(bomb.getPosition(),positionsAround,i,now);
	        			for (Position p : positionToSupp) {
	        				//Apparition of explosions
	        				Explosion exp = new Explosion(game,p,now);
	        				game.addExplosion(i,exp);
	        				if (i==game.getCurrentLevel()) {
		        				spritesExplosion.add(SpriteFactory.createExplosion(layer,exp));
	        				}
	        				
	        			}
	        			//the bomb doesn't exists in the game anymore, we have to remove it from the bombList
	        		    iter.remove();
	        		}
	        	} else {
	        		//the bomb hasn't been put in the game yet
        			spritesBomb.add(SpriteFactory.createBomb(layer, bomb));
	        		bomb.setHasBeenCreated();
	        	}	
	        }
        	
        	Iterator<Explosion> iter2=game.getExplosion().get(i).iterator();
        	while (iter2.hasNext()) {
        		Explosion exp=iter2.next();
        		if (exp.getExplosed()) {
        			//deleting the already explosed
        			iter2.remove();
        		} else {
        			exp.update(now);
        		}
        	}
        	
        	//updating monsters
        	for(Monster monster : game.getMonsters()) {
            	monster.update(now);
            }
        }
        
        //removal of dead/expired objects
        removeSpritesofMissingObjects();

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
        
    }
    
    /**
     * deletes sprites of the Objects (bomb, monster, explosion) that don't exist anymore
     */
    public void removeSpritesofMissingObjects() {
    	//remove sprites of already seen explosions
	    Iterator<SpriteExplosion> it=spritesExplosion.iterator();
	    while (it.hasNext()) {
	        SpriteExplosion next=it.next();
	       	if (next.getExplosion().getExplosed()) {
	       		next.remove();
	       		it.remove();
	       	}
        }
        
        
        //remove sprites of exploded bombs 
	    Iterator<SpriteBomb> iterator = spritesBomb.iterator();
	    while(iterator.hasNext()) {
	        SpriteBomb next = iterator.next();
	       	if(next.getBomb().exploded()) {
	       		next.remove();
	       		iterator.remove();
	       	}
        }
        
	    //remove sprites of dead monsters
        Iterator<SpriteMonster> iterator2 = spritesMonster.iterator();
	    while(iterator2.hasNext()) {
	    	SpriteMonster next2 = iterator2.next();
        	if(!next2.getMonster().isAlive()) {
	      		next2.remove();
	        	iterator2.remove();
	        }
	    }
    }
    
    private void render() {
        spritesDecor.forEach(Sprite::render);
        spritesMonster.forEach(SpriteMonster::render);
        spritesBomb.forEach(SpriteBomb::render);
        spritesExplosion.forEach(SpriteExplosion::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        
    }

    public void start() {
        gameLoop.start();
    }
}
