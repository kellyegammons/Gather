package com.gather.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BaseActor extends Group {
	
	//static
	private static Rectangle worldBounds;
	
	//animation
	private Animation<TextureRegion> animation;
	private float elapsedTime; //incremented by fps
	private boolean animationPaused;
	private float animFrameDuration;
	
	//movement
	private Vector2 velocityVec;
	private Vector2 accelerationVec;
	private float acceleration;
	private float maxSpeed;
	private float deceleration;
	
	//collision
	private Polygon boundaryPolygon;
	
	/*
	 * constructor
	 */
	public BaseActor(float x, float y, Stage s) {
		super();
		
		//set position and add to stage
		setPosition(x,y);
		s.addActor(this);
		
		//scale up (pixels are small on modern monitors)
		this.scaleBy(1f, 1f);
		
		//initialize animation variables
		animation = null;
		elapsedTime = 0;
		animationPaused = false;
		animFrameDuration = 0.2f;
		
		//initialize movement variables
		velocityVec = new Vector2(0, 0);
		accelerationVec = new Vector2(0, 0);
		maxSpeed = 1000;
		deceleration = 0;
	}
	
	public BaseActor() {
		super();
		
		//scale up (pixels are small on modern monitors)
		this.scaleBy(1f, 1f);
		
		//initialize animation variables
		animation = null;
		elapsedTime = 0;
		animationPaused = false;
		animFrameDuration = 0.2f;
		
		//initialize movement variables
		velocityVec = new Vector2(0, 0);
		accelerationVec = new Vector2(0, 0);
		maxSpeed = 1000;
		deceleration = 0;
	}
	
	/*
	 * with regards to multiple Actors on stage
	 * 
	 */
	public static ArrayList<BaseActor> getList(Stage stage, String className) {
		
		//list of actors
		ArrayList<BaseActor> list = new ArrayList<BaseActor>();
		
		//the class
		@SuppressWarnings("rawtypes")
		Class theClass = null;
		
		//try to get the name from the string
		//exception if the class does not exist
		try {
			theClass = Class.forName(className);
		} catch(Exception error) {
			error.printStackTrace();
		}
		
		//check if each actor ont he stage is an instance of the class in question (eg: Person)
		for(Actor a : stage.getActors()) {
			if(theClass.isInstance(a)) {
				list.add((BaseActor)a);//add them to the list
			}
		}
		
		return list;
	}
	
	public static int count(Stage stage, String className) {
		return getList(stage, className).size();
	}
	
	/*
	 * positioning
	 * 
	 */
	public void centerAtPosition(float x, float y) {
		setPosition(x - getWidth()/2, y - getHeight()/2);
	}
	
	public void centerAtActor(BaseActor other) {
		centerAtPosition(other.getX() + other.getWidth()/2, other.getY() + other.getHeight()/2);
	}
	
	/*
	 * animation
	 * 
	 */
	public void setAnimation(Animation<TextureRegion> anim) {
		animation = anim;
		TextureRegion tr = animation.getKeyFrame(0);
		float w = tr.getRegionWidth();//width
		float h = tr.getRegionHeight();//height
		setSize(w,h);//width and height
		setOrigin(w/2, h/2);//width divided by 2, height divided by 2 = center of x,y
		
		//set boundaries for collision
		if(boundaryPolygon == null) {
			setBoundaryRectangle();
		}
	}
	
	public void setAnimationPaused(boolean pause) {
		animationPaused = pause;
	}
	
	// creates an animation from separate image files
	// 1. takes a string array of file names, a duration (float), and boolean for whether or not the animation should loop
	// 2. sets the animation as this object's animation if it is null
	// 3. returns an animation object
	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop){
		int fileCount = fileNames.length;
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		for(int n = 0; n < fileCount; n++) {//loop through the filenames and add them as TextureRegions to the texture Array
			String fileName = fileNames[n];//get the file name
			Texture texture = new Texture(Gdx.files.internal(fileName));//make a new texture using the file
			texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);//set Linear filter
			textureArray.add(new TextureRegion(texture));//ad this texture as a new TextureRegion to the textureArray
		}
		
		// Animation is a collection(?) of generics and in this case will be a TextureRegion which provides more tools than Texture
		// the arguments given are the frameDuration and the textureArray created from the above loop
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		
		if(loop) {//if loop is true, set the animation mode to loop otherwise set it to normal
			anim.setPlayMode(Animation.PlayMode.LOOP);
		} else {
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		}
		
		if(animation == null) {//if the animation of the object is null
			setAnimation(anim);//set the animation tot he one created in this method
		}
		
		return anim; //return the animation
	}
	
	// creates an animation from a spritesheet
	// 1. takes a string as the file name, integers for the rows and columns, float for duration of each frame, and a boolean for whether or no the animation shouldloop
	// 2. sets the animation as this object's animation if it is null
	// 3. returns an animation object
	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop){
		Texture texture = new Texture(Gdx.files.internal(fileName), true);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		int frameWidth = texture.getWidth() / cols;
		int frameHeight = texture.getHeight() / rows;
		
		//multi dimentional array which stores the grid of sub-images calculated by the split() method of TextureRegion
		TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
		
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		//nested loop for the multi dimentional array "temp"
		for(int r = 0; r < rows; r++) {//r = rows
			for(int c = 0; c < cols; c++) {//c = columns
				textureArray.add(temp[r][c]);//add to the textureArray
			}
		}
		
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);//new animation using the frame duration and textureArray as arguments
		
		if(loop) {//if loop is true, set the animation mode to loop otherwise set it to normal
			anim.setPlayMode(Animation.PlayMode.LOOP);
		} else {
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		}
		
		if(animation == null) {//if the animation of the object is null
			setAnimation(anim);//set the animation tot he one created in this method
		}
		
		return anim; //return the animation
		
	}
	
	
	//shortcut for creating a single frame animation from the loadAnimationFromFiles() method
	public Animation<TextureRegion> loadTexture(String fileName){
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}
	
	//check if animation is finished on the actor object
	public boolean isAnimationFinished() {
		return animation.isAnimationFinished(elapsedTime);
	}
	
	//get frame duration
	public float getFrameDuration() {
		return this.animFrameDuration;
	}
	
	//set frame duration
	public void setFrameDuration(float fd) {
		this.animFrameDuration = fd;
		this.animation.setFrameDuration(fd);
	}
	
	/*
	 * effects
	 * 
	 */
	public void setOpacity(float opacity) {
		this.getColor().a = opacity;
	}
	
	/*
	 * movement
	 * 
	 */
	public void setSpeed(float speed) {
		//if length is zero, then assume motion angle is zero degress
		if(velocityVec.len() == 0) {
			velocityVec.set(speed, 0);
		} else {
			velocityVec.setLength(speed);
		}
	}
	
	public float getSpeed() {
		return velocityVec.len();
	}
	
	public void setMotionAngle(float angle) {
		velocityVec.setAngle(angle);
	}
	
	public float getMotionAngle() {
		return velocityVec.angle();
	}
	
	public boolean isMoving() {
		return (getSpeed() > 0);
	}
	
	public void setAcceleration(float acc) {
		acceleration = acc;
	}
	
	public void accelerateAtAngle(float angle) {
		accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
	}
	
	public void accelerateForward() {
		accelerateAtAngle(getRotation());
	}
	
	public void setMaxSpeed(float ms) {
		maxSpeed = ms;
	}
	
	public void setDeceleration(float dec) {
		deceleration = dec;
	}
	
	public void applyPhysics(float dt) {
		
		//apply acceleration
		velocityVec.add( accelerationVec.x * dt, accelerationVec.y * dt );
		
		float speed = getSpeed();
		
		//decrease speed (decelerate) when not accelerating
		if(accelerationVec.len() == 0) {
			speed -= deceleration * dt;
		}
		
		//keep speed within set bounds
		speed = MathUtils.clamp(speed, 0, maxSpeed);
		
		//update velocity
		setSpeed(speed);
		
		//apply velocity
		moveBy( velocityVec.x * dt, velocityVec.y * dt );
		
		//recet acceleration
		accelerationVec.set(0,0);
	}
	
	/*
	 * collision
	 * 
	 */
	public void setBoundaryRectangle() {
		float w = getWidth();
		float h = getHeight();
		float[] vertices = {0,0,w,0,w,h,0,h};
		boundaryPolygon = new Polygon(vertices);
	}
	
	public void setBoundaryPolygon(int numSides) {
		float w = getWidth();
		float h = getHeight();
		
		float[] vertices = new float[2*numSides];
		for(int i = 0; i < numSides; i++) {
			float angle = i * 6.28f / numSides;
			
			//x-coordinate
			vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
			
			//y-coordinate
			vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
		}
		
		boundaryPolygon = new Polygon(vertices);
	}
	
	public Polygon getBoundaryPolygon() {
		boundaryPolygon.setPosition(getX(), getY());
		boundaryPolygon.setOrigin(getOriginX(), getOriginY());
		boundaryPolygon.setRotation(getRotation());
		boundaryPolygon.setScale(getScaleX(), getScaleY());
		return boundaryPolygon;
	}
	
	public boolean overlaps(BaseActor other) {
		
		if(getStage() == null) {
			return false;
		}
		
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		
		//initial test to improve performance
		//if the rectangles are not touching eachother the polygon calculation wont
		//even occur
		if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
			return false;
		}
		
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}
	
	//moves the actor polygon away from that of another based on a translation vector
	//returns the direction in which the actor was moved, if it was moved
	//does not calculate unless there is an overlap
	public Vector2 preventOverlap(BaseActor other) {
		
		Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
            return null;
        }

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap) {
            return null;
        }

        this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
        return mtv.normal;
	}
	
	/*
	 * boundaries
	 * 
	 */
	public static void setWorldBounds(float width, float height) {
		worldBounds = new Rectangle(0, 0, width, height);
	}
	
	public static void setWorldBounds(BaseActor ba) {
		setWorldBounds(ba.getWidth(), ba.getHeight());
	}
	
	public void boundToWorld() {//requires worldBounds object to be set
		
		//check left edge
		if(getX() < 0) {
			setX(0);
		}
		
		//check right edge
		if(( getX() + getWidth()) > worldBounds.width) {
			setX(worldBounds.width - getWidth());
		}
		
		//check bottom edge
		if(getY() < 0) {
			setY(0);
		}
		
		//check top edge
		if(getY() + getHeight() > worldBounds.height) {
			setY(worldBounds.height - getHeight());
		}
	}
	
	public void wrapAroundWorld() {//requires worldBounds object to be set
		
		if(getX() + getWidth() < 0) {
			setX(worldBounds.width);
		}
		
		if(getX() > worldBounds.width) {
			setX(-getWidth());
		}
		
		if(getY() + getHeight() < 0) {
			setY(worldBounds.height);
		}
		
		if(getY() > worldBounds.height) {
			setY(-getHeight());
		}
	}
	
	/*
	 * camera
	 * 
	 */
	public void alignCamera() {
		
		Camera cam = this.getStage().getCamera();
		Viewport v = this.getStage().getViewport();
		
		//center camera on actor
		cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
		
		//bound camera to layout
		cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
		cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
		cam.update();
	}
	
	/*
	 * act
	 * 
	 */
	public void act(float dt) {//dt = time passed since previous iteration of the game loop
		super.act(dt);
		
		if(!animationPaused) {
			elapsedTime += dt;
		}
	}
	
	/*
	 * draw
	 * 
	 */
	public void draw(Batch batch, float parentAlpha) {
		
		//apply color tint effect
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);
		
		if(animation != null && isVisible()) {
			batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
		
		super.draw(batch, parentAlpha);
	}
	
	

}
