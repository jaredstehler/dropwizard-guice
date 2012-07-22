Dropwizard-Guice
================

This project demonstrates a simple method for integrating Guice with Dropwizard. It uses classpath scanning courtesy of the Reflections project to discover resources to install into the dropwizard environment upon service start.

### Usage

It's simple to use; simple extend from AutoConfigService<? extends Configuration> rather than Service, and optionally override createInjector to provide modules into the guice injector. Now you'll be able to use javax.inject.Inject annotations throughout your project for dependency injection!


