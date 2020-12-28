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
    private final List<List<Sprite>> spritesDecor = new ArrayList<>();
    private List<List<SpriteMonster>> spritesMonster=new ArrayList<>();
    private List<List<SpriteBomb>> spritesBomb=new ArrayList<>();
    private List<List<SpriteExplosion>> spritesExplosion=new ArrayList<>();
    
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
    
    
    
    private boolean isMonsterHere(Position p) {
    	for (Monster monster : game.getMonsters()) {
    		if (monster.getPosition().equals(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private boolean isBehindSomething(Position bombPosition, Position position) {
    	if (Math.abs(bombPosition.x-position.x)<=1 && Math.abs(bombPosition.y-position.y)<=1) {
    		return false;
    	}
    	
    	if(position.x==bombPosition.x) {
    		if(position.y>bombPosition.y) {
    			for(int i=1; i<position.y-bombPosition.y; i++) {
    				Position p = new Position(bombPosition.x,bombPosition.y+i);
    				if( !game.getWorld().isEmpty(p) || isMonsterHere(p) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}else {
    			for(int i=1; i<bombPosition.y-position.y; i++) {
    				Position p = new Position(bombPosition.x,bombPosition.y-i);
    				if( !game.getWorld().isEmpty(p) || isMonsterHere(p) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}
    	}else {
    		if(position.x>bombPosition.x) {
    			for(int i=1; i<position.x-bombPosition.x; i++) {
    				Position p = new Position(bombPosition.x+i,bombPosition.y);
    				if( !game.getWorld().isEmpty(p) || isMonsterHere(p) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}else {
    			for(int i=1; i<bombPosition.x-position.x; i++) {
    				Position p = new Position(bombPosition.x-i,bombPosition.y);
    				if( !game.getWorld().isEmpty(p) || isMonsterHere(p) || player.getPosition().equals(p)) {
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }

    private List<Position> bombDamage(Position bombPosition, List<Position> positionsAround) {
    	List<Position> positionToSupp=new ArrayList<>();
    	Iterator<Position> iterator=positionsAround.iterator();
    	while (iterator.hasNext()) {
    		Position next=iterator.next();
    		
    		if (!isBehindSomething(bombPosition,next) && (game.getWorld().isEmpty(next) || game.getWorld().get(next).canExplose())) {
    			Iterator<Monster> iteratorMonster=game.getMonsters().iterator();
        		while (iteratorMonster.hasNext()) {
        			Monster monster=iteratorMonster.next();
        			if (monster.getPosition().equals(next) && !(isBehindSomething(bombPosition,next))) {
        				monster.setDeath();
        			}
        		}
    			if (player.getPosition().equals(next)) {
        			player.decreaseLives();
        		}
    			positionToSupp.add(next);		
    		}
    	}
    	
    	Iterator<Monster> iteratorMonster=game.getMonsters().iterator();
		while (iteratorMonster.hasNext()) {
			Monster monster=iteratorMonster.next();
			if (!monster.isAlive()) {
				iteratorMonster.remove();
			}
		}
    	
    	Iterator<Position> iterator2=positionToSupp.iterator();
    	while(iterator2.hasNext()) {
    		game.getWorld().clear(iterator2.next());
    	}
    	return positionToSupp;
    }


    private void update(long now) throws IOException {
    	if (game.hasChangedLevel()) {
    		
    		List<Sprite> spritesDecorTemp = new ArrayList<>();
    		List<SpriteMonster> spritesMonsterTemp = new ArrayList<>();
    		List<SpriteBomb> spritesBombTemp = new ArrayList<>();
    		List<SpriteExplosion> spritesExplosionTemp = new ArrayList<>();
    		
    		spritesDecor.get(game.getBeforeLevel()).forEach(s -> spritesDecorTemp.add(s));
    		spritesMonster.get(game.getBeforeLevel()).forEach(s -> spritesMonsterTemp.add(s));
    		spritesBomb.get(game.getBeforeLevel()).forEach(s -> spritesBombTemp.add(s));
    		spritesExplosion.get(game.getBeforeLevel()).forEach(s -> spritesExplosionTemp.add(s));
    		
    		spritesDecor.clear();
    		spritesMonster.clear();
    		spritesBomb.clear();
    		spritesExplosion.clear();
    		
    		spritesDecorTemp.forEach(s -> spritesDecor.get(game.getBeforeLevel()).add(s));
    		spritesMonsterTemp.forEach(s -> spritesMonster.get(game.getBeforeLevel()).add(s));
    		spritesBombTemp.forEach(s -> spritesBomb.get(game.getBeforeLevel()).add(s));
    		spritesExplosionTemp.forEach(s -> spritesExplosion.get(game.getBeforeLevel()).add(s));
    		
    		initialize(stage,game);
    		
    		game.getWorld().forEach( (pos,d) -> spritesDecor.get(game.getCurrentLevel()).add(SpriteFactory.createDecor(layer, pos, d)));
    		game.setChangedLevel(false);
    		
    	}
    	if (game.getWorld().hasChanged()) {
    		spritesDecor.clear();
  
    		
    		game.getWorld().forEach( (pos,d) -> spritesDecor.get(game.getCurrentLevel()).add(SpriteFactory.createDecor(layer, pos, d)));
    		game.getWorld().setChanged(false);
    		}
    	
        player.update(now);
        for(Monster monster : game.getMonsters()) {
        	monster.update(now);
        }
        
        game.getMonsters().forEach(go -> spritesMonster.get(game.getCurrentLevel()).add(SpriteFactory.createMonster(layer, go)));
        
        //update les bombes existantes, crée celles qui ne le sont pas
        for(int i =0; i< game.getNbLevels(); i++) {
	        Iterator<Bomb> iter=player.getListBombs().get(i).iterator();
	        while (iter.hasNext()) {
	        	Bomb bomb=iter.next();
	        	if (bomb.getCreated()) {
	        		if (!bomb.explosed()) {
	            		bomb.update(now);
	        		}else {
	        			List<Position> positionsAround=bomb.positionsAroundBomb(player.getRangeBombs());
	        			List<Position> positionToSupp = bombDamage(bomb.getPosition(),positionsAround);
	        			for (Position p : positionToSupp) {
	        				Explosion exp = new Explosion(game,p,now);
	        				spritesExplosion.get(i).add(SpriteFactory.createExplosion(layer,exp));
	        				game.addExplosion(i,exp);
	        			}
	        		    iter.remove();
	        		}
	        	} else {
	        		spritesBomb.get(i).add(SpriteFactory.createBomb(layer, bomb));
	        		bomb.setCreated();
	        	}	
	        }
        }
        
       
        //update les explosions
        for(int i=0; i<game.getNbLevels(); i++) {
        	for (Explosion e : game.getExplosion().get(i))
        		e.update(now);
        }
        
        //enlève les explosions déjà apparues
        for(int i =0; i<game.getNbLevels();i++) {
	        Iterator<SpriteExplosion> it=spritesExplosion.get(i).iterator();
	        while (it.hasNext()) {
	        	SpriteExplosion next=it.next();
	        	if (next.getExplosion().getExplosed()) {
	        		next.remove();
	        		it.remove();
	        	}
	        }
        }
        
        //enlève les sprites des bombes explosées
        for(int i=0; i<game.getNbLevels(); i++) {
	        Iterator<SpriteBomb> iterator = spritesBomb.get(i).iterator();
	        while(iterator.hasNext()) {
	        	SpriteBomb next = iterator.next();
	        	if(next.getBomb().explosed()) {
	        		next.remove();
	        		iterator.remove();
	        		player.increaseNbBombs();
	        	}
	        }
        }
        for(int i=0; i<game.getNbLevels(); i++) {
        Iterator<SpriteMonster> iterator2 = spritesMonster.get(i).iterator();
	        while(iterator2.hasNext()) {
	        	SpriteMonster next2 = iterator2.next();
	        	if(!next2.getMonster().isAlive()) {
	        		next2.remove();
	        		iterator2.remove();
	        	}
	        }
        }

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
        
    }
    
    private void render() {
        spritesDecor.get(game.getCurrentLevel()).forEach(Sprite::render);
        spritesMonster.get(game.getCurrentLevel()).forEach(SpriteMonster::render);
        spritesBomb.get(game.getCurrentLevel()).forEach(SpriteBomb::render);
        spritesExplosion.get(game.getCurrentLevel()).forEach(SpriteExplosion::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        
    }

    public void start() {
        gameLoop.start();
    }
}
