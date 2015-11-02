package sbourges.game.gdxtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;

public class GdxTest extends ApplicationAdapter {
    public OrthographicCamera cam;
    public ModelBatch modelBatch;
    public AssetManager assets;
	private ParticleEffect currentEffects;
	private ParticleSystem particleSystem;
	
    @Override
    public void create () {
        modelBatch = new ModelBatch();
        
        cam = new OrthographicCamera(18.0f, 18.0f);
        assets = new AssetManager();

        // ParticleSystem initial setup
        particleSystem = ParticleSystem.get();
        PointSpriteParticleBatch pointSpriteBatch = new PointSpriteParticleBatch();
        pointSpriteBatch.setCamera(cam);
        particleSystem = ParticleSystem.get();
        particleSystem.add(pointSpriteBatch);
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        assets.setLoader(ParticleEffect.class, loader);
        assets.load("data/point.pfx", ParticleEffect.class, loadParam);
        assets.finishLoading();

        currentEffects=assets.get("data/point.pfx",ParticleEffect.class).copy();
        currentEffects.init();
        particleSystem.add(currentEffects);
    }

    @Override
        public void render () {
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

            modelBatch.begin(cam);
            particleSystem.update();
            particleSystem.begin();
            particleSystem.draw();
            particleSystem.end();
            modelBatch.render(particleSystem);
            modelBatch.end();
        }

    @Override
        public void dispose () {
            if (currentEffects!=null) currentEffects.dispose();
            modelBatch.dispose();
            assets.dispose();
        }
}
