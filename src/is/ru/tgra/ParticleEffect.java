package is.ru.tgra;

import java.nio.FloatBuffer;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

public class ParticleEffect
{
	FloatBuffer vertexBuffer;
	FloatBuffer texCoordBuffer;
	
	Texture tex;
	
	private class Particle
	{
		public Vector3D speed;
		public Point3D position;
		public float timeLived;
		public float timeToLive;
		public float orientationX;
		public float orientationY;
	}
	
	Particle[] particles;
	int particleCount = 600;
	
	public ParticleEffect()
	{
			vertexBuffer = BufferUtils.newFloatBuffer(12);
			vertexBuffer.put(new float[] {-0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f,
										  0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f});
			vertexBuffer.rewind();
			
			texCoordBuffer = BufferUtils.newFloatBuffer(8);
			texCoordBuffer.put(new float[] {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f});
			texCoordBuffer.rewind();
			
			tex = new Texture(Gdx.files.internal("assets/textures/star03.bmp"));
			
			particles = new Particle[particleCount];
			for(int i = 0; i < particleCount; i++)
			{
				particles[i] = new Particle();
				particles[i].timeLived = 0;
				particles[i].timeToLive = (float)Math.random() * 1.5f + 1.2f;
				particles[i].speed = new Vector3D(((float)Math.random() - 0.5f) * 1.7f, (float)Math.random() + 0.3f, ((float)Math.random() - 0.5f) * 1.7f);
				particles[i].position = new Point3D(0.0f, 0.0f, 0.0f);
				particles[i].orientationX = (float)Math.random() * 180.0f;
				particles[i].orientationY = (float)Math.random() * 180.0f;
			}
	}
	
	public void update(float deltaTime)
	{
		for(int i = 0; i < particleCount; i++)
		{
			if(particles[i].timeLived < particles[i].timeToLive)
			{
				particles[i].position.add(Vector3D.mult(deltaTime, particles[i].speed));
				particles[i].speed.x = particles[i].speed.x - particles[i].speed.x * (1.0f - (particles[i].timeToLive - particles[i].timeLived) / particles[i].timeToLive);
				particles[i].speed.z = particles[i].speed.z - particles[i].speed.z * (1.0f - (particles[i].timeToLive - particles[i].timeLived) / particles[i].timeToLive);
				

				particles[i].position.x = particles[i].position.x - particles[i].position.x * (1.0f - (particles[i].timeToLive - particles[i].timeLived) / particles[i].timeToLive);
				particles[i].position.z = particles[i].position.z - particles[i].position.z * (1.0f - (particles[i].timeToLive - particles[i].timeLived) / particles[i].timeToLive);
				particles[i].timeLived += deltaTime;
			}
			else
			{
				particles[i].timeLived = 0;
				particles[i].timeToLive = (float)Math.random() * 1.5f + 1.2f;
				particles[i].speed = new Vector3D(((float)Math.random() - 0.5f) * 1.7f, (float)Math.random() + 0.3f, ((float)Math.random() - 0.5f) * 1.7f);
				particles[i].position = new Point3D(0.0f, 0.0f, 0.0f);
				particles[i].orientationX = (float)Math.random() * 180.0f;
				particles[i].orientationY = (float)Math.random() * 180.0f;
			}
		}
	}
	
	public void display()
	{

		Gdx.gl11.glShadeModel(GL11.GL_SMOOTH);
		Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
		
		Gdx.gl11.glEnable(GL11.GL_TEXTURE_2D);
		Gdx.gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		tex.bind(); 

		Gdx.gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texCoordBuffer);

		Gdx.gl11.glEnable(GL11.GL_BLEND);
		Gdx.gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);

		Gdx.gl11.glDisable(GL11.GL_DEPTH_TEST);

		float[] materialEm = {1.0f, 1.0f, 1.0f, 1.0f};
		Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_EMISSION, materialEm, 0);


		for(int i = 0; i < particleCount; i++)
		{

			float intensity;
			if(particles[i].timeLived < 0.5)
			{
				intensity = particles[i].timeLived / 0.5f;
			}
			else if(particles[i].timeLived < particles[i].timeToLive - 1.2)
			{
				intensity = 1.0f;
			}
			else
			{
				intensity = 1 - ((particles[i].timeLived - (particles[i].timeToLive - 1.2f)) / 1.2f);
			}
			
			float[] materialDiffuse = {0.0f, 0.0f, 0.0f, intensity * 0.15f};
			Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse, 0);
			
			
			Gdx.gl11.glPushMatrix();
			Gdx.gl11.glTranslatef(particles[i].position.x, particles[i].position.y, particles[i].position.z);
			Gdx.gl11.glScalef(intensity * 0.3f, intensity * 0.3f, intensity * 0.3f);
			Gdx.gl11.glRotatef(particles[i].orientationX, 1.0f, 0.0f, 0.0f);
			Gdx.gl11.glRotatef(particles[i].orientationY, 0.0f, 1.0f, 0.0f);
			Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			Gdx.gl11.glPopMatrix();
		}
		

		materialEm[3] = 0.0f;
		Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_EMISSION, materialEm, 0);


		Gdx.gl11.glEnable(GL11.GL_DEPTH_TEST);
		Gdx.gl11.glDisable(GL11.GL_BLEND);
		Gdx.gl11.glDisable(GL11.GL_TEXTURE_2D);
		Gdx.gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		float[] materialEm_def = {0.0f, 0.0f, 0.0f, 0.0f};
		Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_EMISSION, materialEm_def, 0);
		
		
	}
}
