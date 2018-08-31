package com.puffin.infrastructure.swagger

import com.fasterxml.classmate.TypeResolver
import com.puffin.CinemaApplication
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.env.Environment
import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, WebMvcConfigurer}
import springfox.documentation.builders.{PathSelectors, RequestHandlerSelectors}
import springfox.documentation.schema.{AlternateTypeRule, AlternateTypeRules, WildcardType}
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import scala.concurrent.Future

@EnableSwagger2
@Configuration
class SwaggerConfig extends WebMvcConfigurer {

  override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
  }

  @Bean
  def docket(
    typeResolver: TypeResolver,
    env: Environment
  ): Docket = {

    new Docket(DocumentationType.SWAGGER_2)
      .alternateTypeRules(scalaTypes(typeResolver): _*)
      .useDefaultResponseMessages(false)
      .select
      .apis(RequestHandlerSelectors.basePackage(classOf[CinemaApplication].getPackage.getName))
      .paths(PathSelectors.any)
      .build
  }

  def scalaTypes(typeResolver: TypeResolver) = {

    val seqRule = new AlternateTypeRule(
      typeResolver.resolve(classOf[scala.collection.Seq[_]], classOf[WildcardType]), // From type
      typeResolver.resolve(classOf[java.util.List[_]], classOf[WildcardType])
    )

    val mapRule = new AlternateTypeRule(
      typeResolver
        .resolve(classOf[scala.collection.Map[_, _]], classOf[WildcardType], classOf[WildcardType]), // From type
      typeResolver.resolve(classOf[java.util.Map[_, _]], classOf[WildcardType], classOf[WildcardType])
    ) // To type

    val immMapRule = new AlternateTypeRule(
      typeResolver
        .resolve(classOf[scala.collection.immutable.Map[_, _]], classOf[WildcardType], classOf[WildcardType]), // From type
      typeResolver.resolve(classOf[java.util.Map[_, _]], classOf[WildcardType], classOf[WildcardType])
    ) // To type

    val immSeqRule = new AlternateTypeRule(
      typeResolver.resolve(classOf[scala.collection.immutable.Seq[_]], classOf[WildcardType]), // From type
      typeResolver.resolve(classOf[java.util.List[_]], classOf[WildcardType])
    )

    val list = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[List[_]], classOf[WildcardType]),
      typeResolver.resolve(classOf[java.util.List[_]], classOf[WildcardType])
    )

    val set = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[scala.collection.Set[_]], classOf[WildcardType]),
      typeResolver.resolve(classOf[java.util.List[_]], classOf[WildcardType])
    )

    val set2 = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[Set[_]], classOf[WildcardType]),
      typeResolver.resolve(classOf[java.util.Set[_]], classOf[WildcardType])
    )

    val futureList = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[Future[_]], typeResolver.resolve(classOf[List[_]], classOf[WildcardType])),
      typeResolver.resolve(classOf[java.util.List[_]], classOf[WildcardType]),
      org.springframework.core.Ordered.LOWEST_PRECEDENCE - 1
    )

    val future = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[Future[_]], classOf[WildcardType]),
      typeResolver.resolve(classOf[WildcardType])
    )

    val scalaOption = AlternateTypeRules.newRule(
      typeResolver.resolve(classOf[scala.Option[_]], classOf[WildcardType]), // From type
      typeResolver.resolve(classOf[WildcardType])
    )

    Array(future, list, seqRule, mapRule, immMapRule, immSeqRule, futureList, set, set2, scalaOption)
  }
}
