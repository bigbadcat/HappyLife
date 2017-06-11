package org.xuxiang.happylife.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWitherBomb extends EntityThrowable{
	
	public EntityWitherBomb(World worldIn)
    {
        super(worldIn);
    }

    public EntityWitherBomb(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityWitherBomb(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null){
            int i = 0;
            if (mop.entityHit instanceof EntityBlaze){
                i = 10;
            }
            mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
        }else{
        	if (!this.worldObj.isRemote) {
        		this.worldObj.destroyBlock(mop.getBlockPos(), true);
            }
        }
		
		if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
		
	}
}
