/*
 * MIT License
 *
 * Copyright (c) 2020 - present Aegis GIS, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.aegis.commerce.bus;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.reflect.GenericTypeUtils;
import io.micronaut.inject.BeanDefinition;

import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Singleton
public class Registry {

    private final Map<Class<? extends Command>, CommandHandlerProvider> commandHandlerProviders = new HashMap<>();
    private final Map<Class<? extends Query>, QueryHandlerProvider> queryHandlerProviders = new HashMap<>();

    public Registry(ApplicationContext applicationContext) {
        Collection<BeanDefinition<CommandHandler>> commandHandlers = applicationContext.getBeanDefinitions(CommandHandler.class);
        commandHandlers.forEach(h -> registerCommandHandler(applicationContext, h));

        Collection<BeanDefinition<QueryHandler>> queryHandlers = applicationContext.getBeanDefinitions(QueryHandler.class);
        queryHandlers.forEach(h -> registerQueryHandler(applicationContext, h));
    }

    private void registerCommandHandler(ApplicationContext applicationContext, BeanDefinition<CommandHandler> commandHandler) {
        Class<?>[] handlerTypes = GenericTypeUtils.resolveInterfaceTypeArguments(commandHandler.getBeanType(), CommandHandler.class);
        Class<? extends Command> commandType = (Class<? extends Command>) handlerTypes[1];
        commandHandlerProviders.put(commandType, new CommandHandlerProvider(applicationContext, commandHandler.getBeanType()));
    }

    private void registerQueryHandler(ApplicationContext applicationContext, BeanDefinition<QueryHandler> queryHandler) {
        Class<?>[] handlerTypes = GenericTypeUtils.resolveInterfaceTypeArguments(queryHandler.getBeanType(), QueryHandler.class);
        Class<? extends Query> queryType = (Class<? extends Query>) handlerTypes[1];
        queryHandlerProviders.put(queryType, new QueryHandlerProvider(applicationContext, queryHandler.getBeanType()));
    }

    <R, C extends Command<R>> CommandHandler<R, C> getCommandHandler(Class<C> commandType) {
        return commandHandlerProviders.get(commandType).get();
    }

    <R, Q extends Query<R>> QueryHandler<R, Q> getQueryHandler(Class<Q> queryType) {
        return queryHandlerProviders.get(queryType).get();
    }

}
