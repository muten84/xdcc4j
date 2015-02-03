package it.luigibifulco.xdcc4j.search.parser;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.name.Names;

public class ParserFactoryModule extends AbstractModule {

	@Retention(RetentionPolicy.RUNTIME)
	@BindingAnnotation
	public @interface XdccParserType {
		String type();
	}

	static class XdccItParserTypeImpl implements XdccParserType, Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7598855332554428876L;
		private String t;

		XdccItParserTypeImpl(String t) {
			this.t = t;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return XdccParserType.class;
		}

		@Override
		public String type() {
			return this.t;
		}

	}

	public static XdccParserType typeOf(final String type) {
		return new XdccItParserTypeImpl(type);
	}

	@Override
	protected void configure() {

		bind(XdccHtmlParser.class).annotatedWith(Names.named("xdccit")).to(
				XdccItParser.class);
		bind(XdccHtmlParser.class).annotatedWith(Names.named("xdccfinder")).to(
				XdccFinderParser.class);
	}
}
