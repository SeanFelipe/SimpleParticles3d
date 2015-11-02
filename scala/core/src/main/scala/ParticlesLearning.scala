package kbn.scala.base

import com.badlogic.gdx.{ Gdx, ApplicationAdapter }
import com.badlogic.gdx.graphics.{ GL20, PerspectiveCamera }
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.particles.{ 
    ParticleEffect, ParticleSystem, ParticleEffectLoader
}
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver


class Libgdxbase extends ApplicationAdapter {
    
    lazy val cam = new PerspectiveCamera(60f, 60f, 60f)
    lazy val particleSystem = ParticleSystem.get()
    lazy val pointSpriteBatch = new PointSpriteParticleBatch()
    lazy val assets = new AssetManager()
    lazy val loader = new ParticleEffectLoader(new InternalFileHandleResolver())
    lazy val loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(
        particleSystem.getBatches()
    );
    lazy val modelBatch = new ModelBatch()

    var effect, originalEffect : ParticleEffect = _

    override def create() {
        pointSpriteBatch.setCamera(cam)
        particleSystem.add(pointSpriteBatch)
        assets.setLoader(classOf[ParticleEffect], loader);
        assets.load("particle/point.pfx", classOf[ParticleEffect], loadParam)
        assets.finishLoading()

        originalEffect = assets.get("particle/point.pfx", classOf[ParticleEffect])
        effect = originalEffect.copy()
        effect.init()
        effect.start()  // optional: particle will begin playing immediately
        particleSystem.add(effect)
    }

    override def render() {
        Gdx.gl.glClearColor(0, 0, 0, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        particleSystem.update() // technically not necessary for rendering
        particleSystem.begin()
        particleSystem.draw()
        particleSystem.end()
        modelBatch.render(particleSystem)
    }
}
