package com.example.demo;


import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();
        var newList = list.stream().filter(player -> player.age > 35)
                .collect(Collectors.groupingBy(Player::getClub));

        newList.forEach((club, lista) -> {
            System.out.println("Club: " + club);
            lista.forEach(j -> System.out.println("- " + j.getName() + " (" + j.getAge() + " años)"));
        });
    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();

        var mejorJugador = list.stream()
                .filter(j -> j.getNational().equals("France"))
                        .max(Comparator.comparing(Player::getWinners));

        System.out.println(mejorJugador);
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<String>> clubesporNacionalidad = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.mapping(Player::getClub, Collectors.toList())));

        clubesporNacionalidad.forEach((nationality, clubs) -> {
            System.out.println("* País : "+nationality + " -> ");
            clubs.forEach(club -> System.out.println("\t- Club : "+club));
        });
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = list.stream().max(Comparator.comparing(Player::getWinners));

        System.out.println("Club: "+ mejorJugador.get().getClub()+", Name: "+ mejorJugador.get().getName() + ", Winners:" + mejorJugador.get().getWinners());
    }

    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        var newList = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.maxBy(Comparator.comparing(Player::getWinners))));

        newList.forEach((nacionalidad, jugador) -> {
            if (jugador.isPresent()) {
                System.out.println("Mejor jugador de " + nacionalidad + ": " + jugador.get().getName());
            }
        });
    }
}
