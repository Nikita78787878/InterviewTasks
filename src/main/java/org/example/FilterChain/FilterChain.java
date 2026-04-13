package org.example.FilterChain;

import org.example.RateLimiter.RateLimiter;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**

 * ЗАДАНИЕ: Реализовать механизм Filter Chain с поддержкой порядка выполнения.
 *
 * Контекст:
 *
 * В веб-фреймворках (например, Servlet API или Spring) запрос проходит через
 * цепочку фильтров. Каждый фильтр может:
 *
 * * выполнить какую-то логику
 * * передать управление следующему фильтру
 * * остановить выполнение цепочки
 *
 * Например:
 * Request -> LoggingFilter -> AuthFilter -> RateLimitFilter -> Controller
 *
 * ВАЖНОЕ УСЛОВИЕ:
 *
 * Каждый фильтр имеет числовой order (приоритет выполнения).
 * Чем меньше значение order — тем раньше выполняется фильтр.
 *
 * Требования:
 *
 * 1. Интерфейс Filter
 *
 * Каждый фильтр должен реализовывать метод:
 *
 void doFilter(Request request, Response response, FilterChain chain)
 *
 * А также иметь метод:
 * int getOrder()
 *
 * 2. Регистрация фильтров
 *
 * Фильтры могут регистрироваться в любом порядке:
 *
 register(AuthFilter) -- обязательный
 register(LoggingFilter) -- обязательный
 register(RateLimitFilter)
 (можно придумать любые свои фильтры)
 *
 * 3. Построение цепочки
 * 4. Выполнение цепочки
 *
 * FilterChain должен вызывать фильтры по порядку order.
 *
 * Каждый фильтр может:
 *
 - вызвать следующий фильтр:
 *
 chain.doFilter(request, response)
 *
 - остановить выполнение цепочки (не вызывая chain).
 *
 * 5. Завершение цепочки
 *
 * Если фильтры закончились — выполнение должно завершиться.
 * Всегда нужно выводить имя последнего выполненного фильтра (если цепочка остановилась или
 * успешно завершилась до конца).
 *
 *
 */

// TODO ДЗ если у на будет эксепшен на фильтре что делать тогда!
public class FilterChain {
    List<Filter> filters = new ArrayList<>();
    private int index = 0;
    private Filter lastExecuted;

    public void doFilter(Request request, Response response) {
        try{
            if(index < filters.size()) {
                Filter filter = filters.get(index++);
                filter.doFilter(request, response, this);
            }
        } catch (Exception e) {
            response.setStatus(500);
            System.out.println("Exception in filter: " + e.getMessage());
        }finally {
            if (index >= filters.size()) {
                System.out.println("Last filter: "
                        + (lastExecuted != null ? lastExecuted.getClass().getSimpleName() : "none"));
                index = 0;
            }
        }
    }

    public void registerFilter(Filter filter){
        filters.add(filter);
    }

    public static void main(String[] args) {

        FilterChain chain = new FilterChain();

        // RateLimiter
        RateLimiter rateLimiter = new RateLimiter(
                5000L,
                Clock.systemDefaultZone()
        );
        rateLimiter.configureLimit("path", 3);

        // регистрация фильтров (в любом порядке!)
        chain.registerFilter(new AuthFilter());
        chain.registerFilter(new LoggingFilter());
        chain.registerFilter(new RateLimitFilter(rateLimiter));

        chain.buildFilters();

        // имитация запросов
        for (int i = 0; i < 6; i++) {
            System.out.println("\n--- REQUEST " + i + " ---");

            Response response = new Response();

            chain.doFilter(
                    new Request("path", "user"),
                    response
            );

            System.out.println("Response status: " + response.getStatus());
        }
    }

    public void buildFilters(){
        filters.sort(Comparator.comparingInt(Filter::getOrder));
    }
}










