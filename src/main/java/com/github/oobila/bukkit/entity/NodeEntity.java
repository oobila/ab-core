package com.github.oobila.bukkit.entity;

import com.github.oobila.bukkit.util.VectorMaths;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class NodeEntity<T extends Entity> extends CustomEntity<T> {

    @Getter(AccessLevel.PROTECTED)
    private NodeEntity<?> parent;
    @Getter(AccessLevel.PROTECTED)
    private Map<UUID, NodeEntity<Entity>> children = new HashMap<>();
    private Vector offset;
    private EulerAngle angle;
    private CustomEntityBehaviour<NodeEntity<T>> behaviour;

    protected NodeEntity(T entity, CustomEntityBehaviour<NodeEntity<T>> behaviour) {
        super(entity);
        if(behaviour != null){
            this.behaviour = behaviour;
            BehaviourScheduler.registerNodeEntity(this);
        }
    }

    public void attachChild(NodeEntity<Entity> child, Vector offset, EulerAngle angle){
        children.put(child.getUniqueId(), child);
        child.parent = this;
        child.offset = offset;
        child.angle = angle;
    }

    public NodeEntity<Entity> detachChild(UUID id){
        NodeEntity<Entity> r = children.remove(id);
        r.parent = null;
        return r;
    }

    public NodeEntity<Entity> detachChild(NodeEntity<Entity> child){
        return detachChild(child.getUniqueId());
    }

    private void updateRotationalPosition(){
        children.values().forEach(c -> {
            c.setRotation(getLocation().getPitch(), getLocation().getYaw());
            float yaw = getLocation().getYaw();
            Vector newOffset = VectorMaths.rotate2d(offset, yaw);
            c.teleport(getLocation().add(newOffset));
            //TODO update position based off new pitch
            c.updateRotationalPosition();
        });
    }

    private void updatePosition(){
        children.values().forEach(c -> {
            c.teleport(c.parent.getLocation().add(c.offset));
            c.updatePosition();
        });
    }

    @Override
    public void setVelocity(Vector vector) {
        super.setVelocity(vector);
        children.values().forEach(c -> c.setVelocity(vector));
    }

    @Override
    public void setRotation(float v, float v1) {
        super.setRotation(v, v1);
        updateRotationalPosition();
    }

    @Override
    public boolean teleport(Location location) {
        if(super.teleport(location)) {
            updatePosition();
            return true;
        }
        return false;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        if(super.teleport(location, teleportCause)) {
            updatePosition();
            return true;
        }
        return false;
    }

    @Override
    public boolean teleport(Entity entity) {
        if(super.teleport(entity)) {
            updatePosition();
            return true;
        }
        return false;
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        if(super.teleport(entity, teleportCause)) {
            updatePosition();
            return true;
        }
        return false;
    }

    @Override
    public void setFireTicks(int i) {
        super.setFireTicks(i);
        children.values().forEach(c -> c.setFireTicks(i));
    }

    @Override
    public void setVisualFire(boolean b) {
        super.setVisualFire(b);
        children.values().forEach(c -> c.setVisualFire(b));
    }

    @Override
    public void setFreezeTicks(int i) {
        super.setFreezeTicks(i);
        children.values().forEach(c -> c.setFreezeTicks(i));
    }

    @Override
    public void remove() {
        if(behaviour != null){
            BehaviourScheduler.remove(this);
        }
        children.values().forEach(NodeEntity::remove);
        super.remove();
    }

    @Override
    public void setPersistent(boolean b) {
        super.setPersistent(b);
        children.values().forEach(c -> c.setPersistent(b));
    }

    @Override
    public void setFallDistance(float v) {
        super.setFallDistance(v);
        children.values().forEach(c -> c.setFallDistance(v));
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        super.setLastDamageCause(entityDamageEvent);
        children.values().forEach(c -> c.setLastDamageCause(entityDamageEvent));
    }

    @Override
    public void setTicksLived(int i) {
        super.setTicksLived(i);
        children.values().forEach(c -> c.setTicksLived(i));
    }

    @Override
    public void setGlowing(boolean b) {
        super.setGlowing(b);
        children.values().forEach(c -> c.setGlowing(b));
    }

    @Override
    public void setInvulnerable(boolean b) {
        super.setInvulnerable(b);
        children.values().forEach(c -> c.setInvulnerable(b));
    }

    @Override
    public void setSilent(boolean b) {
        super.setSilent(b);
        children.values().forEach(c -> c.setSilent(b));
    }

    @Override
    public void setGravity(boolean b) {
        super.setGravity(b);
        children.values().forEach(c -> c.setGravity(b));
    }

    @Override
    public void setPortalCooldown(int i) {
        super.setPortalCooldown(i);
        children.values().forEach(c -> c.setPortalCooldown(i));
    }
}
