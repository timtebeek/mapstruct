/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.internal.model.source;

import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;

import org.mapstruct.ap.internal.option.Options;
import org.mapstruct.ap.internal.prism.BuilderPrism;
import org.mapstruct.ap.internal.prism.CollectionMappingStrategyPrism;
import org.mapstruct.ap.internal.prism.InjectionStrategyPrism;
import org.mapstruct.ap.internal.prism.MapperConfigPrism;
import org.mapstruct.ap.internal.prism.MapperPrism;
import org.mapstruct.ap.internal.prism.MappingInheritanceStrategyPrism;
import org.mapstruct.ap.internal.prism.NullValueCheckStrategyPrism;
import org.mapstruct.ap.internal.prism.NullValueMappingStrategyPrism;
import org.mapstruct.ap.internal.prism.NullValuePropertyMappingStrategyPrism;
import org.mapstruct.ap.internal.prism.ReportingPolicyPrism;

public class MapperOptions extends DelegatingOptions {

    private final MapperPrism prism;
    private final DeclaredType mapperConfigType;

    public static MapperOptions getInstanceOn(TypeElement typeElement, Options options) {
        MapperPrism prism = MapperPrism.getInstanceOn( typeElement );
        MapperOptions mapperAnnotation;
        DelegatingOptions defaults = new DefaultOptions( prism, options );
        DeclaredType mapperConfigType;
        if ( prism.values.config() != null && prism.config().getKind() == TypeKind.DECLARED ) {
            mapperConfigType = (DeclaredType) prism.config();
        }
        else {
            mapperConfigType = null;
        }
        if ( mapperConfigType != null ) {
            Element mapperConfigElement = mapperConfigType.asElement();
            MapperConfigPrism configPrism = MapperConfigPrism.getInstanceOn( mapperConfigElement );
            MapperConfigOptions mapperConfigAnnotation = new MapperConfigOptions( configPrism, defaults );
            mapperAnnotation = new MapperOptions( prism, mapperConfigType, mapperConfigAnnotation );
        }
        else {
            mapperAnnotation = new MapperOptions( prism, null, defaults );
        }
        return mapperAnnotation;
    }

    private MapperOptions(MapperPrism prism, DeclaredType mapperConfigType, DelegatingOptions next) {
        super( next );
        this.prism = prism;
        this.mapperConfigType = mapperConfigType;
    }

    @Override
    public String implementationName() {
        return null == prism.values.implementationName() ? next().implementationName() : prism.implementationName();
    }

    @Override
    public String implementationPackage() {
        return null == prism.values.implementationPackage() ? next().implementationPackage() :
            prism.implementationPackage();
    }

    @Override
    public Set<DeclaredType> uses() {
        return toDeclaredTypes( prism.uses(), next().uses() );
    }

    @Override
    public Set<DeclaredType> imports() {
        return toDeclaredTypes( prism.imports(), next().imports() );
    }

    @Override
    public ReportingPolicyPrism unmappedTargetPolicy() {
        return null == prism.values.unmappedTargetPolicy() ? next().unmappedTargetPolicy() :
            ReportingPolicyPrism.valueOf( prism.unmappedTargetPolicy() );
    }

    @Override
    public ReportingPolicyPrism unmappedSourcePolicy() {
        return null == prism.values.unmappedSourcePolicy() ? next().unmappedSourcePolicy() :
        ReportingPolicyPrism.valueOf( prism.unmappedSourcePolicy() );
    }

    @Override
    public ReportingPolicyPrism typeConversionPolicy() {
        return null == prism.values.typeConversionPolicy() ? next().typeConversionPolicy() :
        ReportingPolicyPrism.valueOf( prism.typeConversionPolicy() );
    }

    @Override
    public String componentModel() {
        return null == prism.values.componentModel() ? next().componentModel() : prism.componentModel();
    }

    @Override
    public MappingInheritanceStrategyPrism getMappingInheritanceStrategy() {
        return null == prism.values.mappingInheritanceStrategy() ? next().getMappingInheritanceStrategy() :
            MappingInheritanceStrategyPrism.valueOf( prism.mappingInheritanceStrategy() );
    }

    @Override
    public InjectionStrategyPrism getInjectionStrategy() {
        return null == prism.values.injectionStrategy() ? next().getInjectionStrategy() :
            InjectionStrategyPrism.valueOf( prism.injectionStrategy() );
    }

    @Override
    public Boolean isDisableSubMappingMethodsGeneration() {
        return null == prism.values.disableSubMappingMethodsGeneration() ?
            next().isDisableSubMappingMethodsGeneration() : prism.disableSubMappingMethodsGeneration();
    }

    // @Mapping, @BeanMapping

    @Override
    public CollectionMappingStrategyPrism getCollectionMappingStrategy() {
        return null == prism.values.collectionMappingStrategy() ?
            next().getCollectionMappingStrategy()
            : CollectionMappingStrategyPrism.valueOf( prism.collectionMappingStrategy() );
    }

    @Override
    public NullValueCheckStrategyPrism getNullValueCheckStrategy() {
        return null == prism.values.nullValueCheckStrategy() ?
            next().getNullValueCheckStrategy()
            : NullValueCheckStrategyPrism.valueOf( prism.nullValueCheckStrategy() );
    }

    @Override
    public NullValuePropertyMappingStrategyPrism getNullValuePropertyMappingStrategy() {
        return null == prism.values.nullValuePropertyMappingStrategy() ?
            next().getNullValuePropertyMappingStrategy()
            : NullValuePropertyMappingStrategyPrism.valueOf( prism.nullValuePropertyMappingStrategy() );
    }

    @Override
    public NullValueMappingStrategyPrism getNullValueMappingStrategy() {
        return null == prism.values.nullValueMappingStrategy() ?
            next().getNullValueMappingStrategy()
            : NullValueMappingStrategyPrism.valueOf( prism.nullValueMappingStrategy() );
    }

    @Override
    public BuilderPrism getBuilderPrism() {
        return null == prism.values.builder() ? next().getBuilderPrism() : prism.builder();
    }

    // @Mapper specific

    public DeclaredType mapperConfigType() {
        return mapperConfigType;
    }

    public boolean hasMapperConfig() {
        return mapperConfigType != null;
    }

    public boolean isValid() {
        return prism.isValid;
    }

    public AnnotationMirror getAnnotationMirror() {
        return prism.mirror;
    }

    @Override
    public boolean hasAnnotation() {
        return true;
    }

}