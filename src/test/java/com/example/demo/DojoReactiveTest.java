package com.example.demo;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;

public class DojoReactiveTest {
    private static final Logger log = LoggerFactory.getLogger((DojoReactiveTest.class));

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }



    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();

        Flux.fromIterable(list).filter(player -> player.age >35)
                .groupBy(Player::getClub)
                .flatMap(group -> group.sort(Comparator.comparing(Player::getClub)))
                .distinct()
                .subscribe(System.out::println);

        /*Flux.fromIterable(list).groupBy(Player::getClub)
                .flatMap(club -> club.filter(player -> player.age >35))
                .distinct()
                .subscribe(System.out::println);*/
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list).filter(player -> player.getNational().equals("France"))
                .reduce((j1, j2) -> j1.getWinners() > j2.getWinners() ? j1 : j2)
                .subscribe(System.out::println);
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list).groupBy(Player::getNational)
                .subscribe(group -> {
                    System.out.println("*Pais : " + group.key());
                    group.subscribe(player -> {
                        System.out.println("\t-Club : " + player.getClub());
                    });
                });
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .reduce((j1, j2) -> j1.getWinners() > j2.getWinners() ? j1 : j2)
                .subscribe(player -> System.out.println("Club: "+ player.getClub()+", Name: "+ player.getName() + ", Winners:" + player.getWinners()));
    }

    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list).groupBy(Player::getNational)
                .flatMap(nacion ->
                        nacion.reduce((j1, j2) -> j1.getWinners() > j2.getWinners() ? j1 : j2))
                .sort(Comparator.comparing(Player::getNational))
                .distinct()
                .subscribe(player ->  System.out.println("Mejor jugador de " + player.getNational() + ": " + player.getName()));
    }
}
