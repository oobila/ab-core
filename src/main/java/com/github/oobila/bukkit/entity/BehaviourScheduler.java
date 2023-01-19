package com.github.oobila.bukkit.entity;

import com.github.oobila.bukkit.CorePlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BehaviourScheduler {

    @Setter(AccessLevel.PRIVATE) @Getter(AccessLevel.PRIVATE)
    private static BehaviourScheduler instance = null;

    private Map<Integer, Map<UUID, NodeEntity<?>>> behaviourTickMap = new HashMap<>();
    private boolean shouldRegisterEntities;
    private Set<NodeEntity<?>> stagingRegister = new HashSet<>();
    private boolean shouldRemoveEntities;
    private Map<Integer, Set<UUID>> stagingRemove = new HashMap<>();

    private int tick = 0;

    public BehaviourScheduler() throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException("BehaviourScheduler is a singleton class and cannot be constructed again");
        }
        BehaviourScheduler.setInstance(this);
        Bukkit.getServer().getScheduler().runTaskTimer(CorePlugin.getInstance(), () -> {

            //register new entities
            if(shouldRegisterEntities){
                stagingRegister.forEach(nodeEntity ->
                    behaviourTickMap.get(nodeEntity.getBehaviour().getT()).put(nodeEntity.getUniqueId(), nodeEntity)
                );
                stagingRegister.clear();
                shouldRegisterEntities = false;
            }

            for (Map.Entry<Integer, Map<UUID, NodeEntity<?>>> entry : behaviourTickMap.entrySet()) {
                if (tick % entry.getKey() == 0) {
                    //perform behaviour
                    entry.getValue().values().forEach(entity -> entity.getBehaviour().onNextTick());
                }
            }

            //remove entities
            if(shouldRemoveEntities){
                stagingRemove.keySet().forEach(key ->
                    stagingRemove.get(key).forEach(uuid ->
                        behaviourTickMap.get(key).remove(uuid)
                    )
                );
                stagingRemove.clear();
                shouldRemoveEntities = false;
            }

        }, 1, 1);
    }

    public static void resetInstance(){
        instance = null;
    }

    static <T extends Entity> void registerNodeEntity(NodeEntity<T> nodeEntity){
        instance.stagingRegister.add(nodeEntity);
        instance.shouldRegisterEntities = true;
        instance.behaviourTickMap.computeIfAbsent(nodeEntity.getBehaviour().getT(), i -> new HashMap<>());
    }

    public static <T extends Entity> void remove(NodeEntity<T> nodeEntity) {
        instance.stagingRemove.computeIfAbsent(nodeEntity.getBehaviour().getT(), i -> new HashSet<>());
        instance.stagingRemove.get(nodeEntity.getBehaviour().getT()).add(nodeEntity.getUniqueId());
        instance.shouldRemoveEntities = true;
    }
}
