package tz.go.tcra.lims.utils.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ModelAssembler implements RepresentationModelAssembler<Object, EntityModel<Object>> {

    @Override
    public EntityModel<Object> toModel(Object entity) {
//        return EntityModel.of(entity, //
//                linkTo(methodOn(Object.class).one().withSelfRel(),
//                linkTo(methodOn(Object.class).lists()).withRel("objList")));
        return null;
    }

    @Override
    public CollectionModel<EntityModel<Object>> toCollectionModel(Iterable<?> entities) {
        return null;
    }

    public Method getMethod() {
        return null;
    }
}
