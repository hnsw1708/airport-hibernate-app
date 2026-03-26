package com.pluralsight.hibernatefundamentals.airport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Airport Entity Tests")
class AirportTest {

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport(1, "Heathrow");
    }

    // ---------------------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Parameterised constructor sets id and name correctly")
        void parameterisedConstructor_setsFields() {
            var a = new Airport(42, "JFK");
            assertAll(
                    () -> assertEquals(42, a.getId()),
                    () -> assertEquals("JFK", a.getName())
            );
        }

        @Test
        @DisplayName("No-arg constructor creates an Airport with default values")
        void noArgConstructor_createsAirportWithDefaults() {
            var a = new Airport();
            assertAll(
                    () -> assertEquals(0, a.getId()),
                    () -> assertNull(a.getName()),
                    () -> assertNotNull(a.getPassengers()),
                    () -> assertTrue(a.getPassengers().isEmpty())
            );
        }

        @Test
        @DisplayName("Parameterised constructor starts with empty passenger list")
        void parameterisedConstructor_startsWithEmptyPassengers() {
            assertNotNull(airport.getPassengers());
            assertTrue(airport.getPassengers().isEmpty());
        }
    }

    // ---------------------------------------------------------------------------
    // getId / setId
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("ID field tests")
    class IdTests {

        @Test
        @DisplayName("getId returns the value set by setId")
        void setId_andGetId_roundTrip() {
            airport.setId(99);
            assertEquals(99, airport.getId());
        }

        @Test
        @DisplayName("setId accepts zero")
        void setId_acceptsZero() {
            airport.setId(0);
            assertEquals(0, airport.getId());
        }

        @Test
        @DisplayName("setId accepts negative value")
        void setId_acceptsNegativeValue() {
            airport.setId(-5);
            assertEquals(-5, airport.getId());
        }

        @Test
        @DisplayName("setId accepts Integer.MAX_VALUE")
        void setId_acceptsMaxInt() {
            airport.setId(Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, airport.getId());
        }
    }

    // ---------------------------------------------------------------------------
    // getName / setName
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Name field tests")
    class NameTests {

        @Test
        @DisplayName("getName returns the value set by setName")
        void setName_andGetName_roundTrip() {
            airport.setName("Gatwick");
            assertEquals("Gatwick", airport.getName());
        }

        @Test
        @DisplayName("setName accepts null")
        void setName_acceptsNull() {
            airport.setName(null);
            assertNull(airport.getName());
        }

        @Test
        @DisplayName("setName accepts empty string")
        void setName_acceptsEmptyString() {
            airport.setName("");
            assertEquals("", airport.getName());
        }

        @Test
        @DisplayName("setName accepts a long name using text block style value")
        void setName_acceptsLongName() {
            String longName = """
                    London Heathrow International Airport - Terminal 5""".strip();
            airport.setName(longName);
            assertEquals(longName, airport.getName());
        }

        @ParameterizedTest(name = "setName({0})")
        @ValueSource(strings = {"LAX", "CDG", "SYD", "DXB"})
        @DisplayName("setName handles various valid IATA-style names")
        void setName_variousValidNames(String name) {
            airport.setName(name);
            assertEquals(name, airport.getName());
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("setName handles null via parameterised source")
        void setName_nullSource(String name) {
            airport.setName(name);
            assertNull(airport.getName());
        }
    }

    // ---------------------------------------------------------------------------
    // addPassenger / getPassengers
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Passenger management tests")
    class PassengerTests {

        @Test
        @DisplayName("addPassenger increases passenger count by one")
        void addPassenger_increasesCount() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            assertEquals(1, airport.getPassengers().size());
        }

        @Test
        @DisplayName("addPassenger stores the correct passenger reference")
        void addPassenger_storesCorrectReference() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            assertTrue(airport.getPassengers().contains(passenger));
        }

        @Test
        @DisplayName("addPassenger can be called multiple times")
        void addPassenger_multiplePassengers() {
            var p1 = new Passenger();
            var p2 = new Passenger();
            var p3 = new Passenger();

            airport.addPassenger(p1);
            airport.addPassenger(p2);
            airport.addPassenger(p3);

            List<Passenger> passengers = airport.getPassengers();
            assertAll(
                    () -> assertEquals(3, passengers.size()),
                    () -> assertTrue(passengers.contains(p1)),
                    () -> assertTrue(passengers.contains(p2)),
                    () -> assertTrue(passengers.contains(p3))
            );
        }

        @Test
        @DisplayName("getPassengers returns an unmodifiable list")
        void getPassengers_returnsUnmodifiableList() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            List<Passenger> passengers = airport.getPassengers();

            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.add(new Passenger()),
                    "Expected UnsupportedOperationException when mutating the returned list");
        }

        @Test
        @DisplayName("getPassengers remove throws UnsupportedOperationException")
        void getPassengers_removeThrowsUnsupportedOperationException() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            List<Passenger> passengers = airport.getPassengers();

            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.remove(passenger));
        }

        @Test
        @DisplayName("getPassengers returns empty unmodifiable list when no passengers added")
        void getPassengers_emptyByDefault() {
            List<Passenger> passengers = airport.getPassengers();

            assertNotNull(passengers);
            assertTrue(passengers.isEmpty());
            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.add(new Passenger()));
        }

        @Test
        @DisplayName("addPassenger allows adding the same passenger twice")
        void addPassenger_allowsDuplicates() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            airport.addPassenger(passenger);

            assertEquals(2, airport.getPassengers().size());
        }
    }
}