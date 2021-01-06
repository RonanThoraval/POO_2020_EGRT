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
     * @return true if a monster is at the position p in level "level"
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
     * @return a list of positions where decor must be delete
     * 
     * This function kills also enemies and decrease lives' player when it is necessary
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
    	// cas 1 : on entre dans un nouveau niveau
    	if (game.hasChangedLevel()) {
    		spritesDecor.clear();
    		spritesMonster.clear();
    		spritesBomb.clear();
    		spritesExplosion.clear();
    		
    		initialize(stage,game);
    		game.setChangedLevel(false);	
    	}
    	
    	//cas 2 : le décor du niveau a été modifié
    	if (game.getWorld().hasChanged()) {
    		spritesDecor.forEach(s -> s.remove());
    		spritesDecor.clear();
    		
    		game.getWorld().forEach( (pos,d) -> spritesDecor.add(SpriteFactory.createDecor(layer, pos, d)));
    		game.getWorld().setChanged(false);
    	}
    	
        player.update(now);
        
        
        //update l'état des bombes et des explosions de tous les niveaux, et crée les sprites des nouvelles bombes et explosions
        for (int i=0 ; i<game.getNbLevels(); i++) {
        	Iterator<Bomb> iter=player.getListBombs().get(i).iterator();
        	while (iter.hasNext()) {
	        	Bomb bomb=iter.next();
	        	if (bomb.getCreated()) {
	        		if (!bomb.explosed()) {
	            		bomb.update(now);
	        		}else {
	    	       		player.increaseNbBombs();
	        			List<Position> positionsAround=bomb.positionsAroundBomb(player.getRangeBombs(),i);
	        			List<Position> positionToSupp = bombDamage(bomb.getPosition(),positionsAround,i,now);
	        			for (Position p : positionToSupp) {
	        				Explosion exp = new Explosion(game,p,now);
	        				game.addExplosion(i,exp);
	        				if (i==game.getCurrentLevel()) {
		        				spritesExplosion.add(SpriteFactory.createExplosion(layer,exp));
	        				}
	        				
	        			}
	        		    iter.remove();
	        		}
	        	} else {
        			spritesBomb.add(SpriteFactory.createBomb(layer, bomb));
	        		bomb.setCreated();
	        	}	
	        }
        	
        	Iterator<Explosion> iter2=game.getExplosion().get(i).iterator();
        	while (iter2.hasNext()) {
        		Explosion exp=iter2.next();
        		if (exp.getExplosed()) {
        			iter2.remove();
        		} else {
        			exp.update(now);
        		}
        	}
        	
        	//update monstres
        	for(Monster monster : game.getMonsters()) {
            	monster.update(now);
            }
        }
        
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
    
    public void removeSpritesofMissingObjects() {
    	//enlève les sprites des explosions déjà apparues
	    Iterator<SpriteExplosion> it=spritesExplosion.iterator();
	    while (it.hasNext()) {
	        SpriteExplosion next=it.next();
	       	if (next.getExplosion().getExplosed()) {
	       		next.remove();
	       		it.remove();
	       	}
        }
        
        
        //enlève les sprites des bombes explosées 
	    Iterator<SpriteBomb> iterator = spritesBomb.iterator();
	    while(iterator.hasNext()) {
	        SpriteBomb next = iterator.next();
	       	if(next.getBomb().explosed()) {
	       		next.remove();
	       		iterator.remove();
	       	}
        }
        
	    //enlève les sprites des monstres morts
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
