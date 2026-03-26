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
    // Constructor tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Parameterised constructor sets id and name")
        void parameterisedConstructorSetsFields() {
            var ap = new Airport(42, "JFK");
            assertAll(
                    () -> assertEquals(42, ap.getId()),
                    () -> assertEquals("JFK", ap.getName())
            );
        }

        @Test
        @DisplayName("No-arg constructor creates airport with default values")
        void noArgConstructorCreatesDefaultAirport() {
            var ap = new Airport();
            assertAll(
                    () -> assertEquals(0, ap.getId()),
                    () -> assertNull(ap.getName()),
                    () -> assertNotNull(ap.getPassengers()),
                    () -> assertTrue(ap.getPassengers().isEmpty())
            );
        }

        @Test
        @DisplayName("Passengers list is initialised and empty after construction")
        void passengersListIsInitialisedEmpty() {
            var ap = new Airport(1, "Gatwick");
            assertNotNull(ap.getPassengers());
            assertTrue(ap.getPassengers().isEmpty());
        }

        @Test
        @DisplayName("Constructor accepts zero as id")
        void constructorAcceptsZeroId() {
            var ap = new Airport(0, "Test");
            assertEquals(0, ap.getId());
        }

        @Test
        @DisplayName("Constructor accepts negative id")
        void constructorAcceptsNegativeId() {
            var ap = new Airport(-5, "NegAirport");
            assertEquals(-5, ap.getId());
        }

        @Test
        @DisplayName("Constructor accepts null name")
        void constructorAcceptsNullName() {
            var ap = new Airport(1, null);
            assertNull(ap.getName());
        }

        @Test
        @DisplayName("Constructor accepts empty string name")
        void constructorAcceptsEmptyName() {
            var ap = new Airport(1, "");
            assertEquals("", ap.getName());
        }
    }

    // ---------------------------------------------------------------------------
    // Getter / Setter tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("setId and getId round-trip")
        void setIdAndGetId() {
            airport.setId(99);
            assertEquals(99, airport.getId());
        }

        @Test
        @DisplayName("setId accepts zero")
        void setIdAcceptsZero() {
            airport.setId(0);
            assertEquals(0, airport.getId());
        }

        @Test
        @DisplayName("setId accepts negative values")
        void setIdAcceptsNegativeValue() {
            airport.setId(-1);
            assertEquals(-1, airport.getId());
        }

        @Test
        @DisplayName("setName and getName round-trip")
        void setNameAndGetName() {
            airport.setName("Schiphol");
            assertEquals("Schiphol", airport.getName());
        }

        @Test
        @DisplayName("setName accepts null")
        void setNameAcceptsNull() {
            airport.setName(null);
            assertNull(airport.getName());
        }

        @Test
        @DisplayName("setName accepts empty string")
        void setNameAcceptsEmptyString() {
            airport.setName("");
            assertEquals("", airport.getName());
        }

        @ParameterizedTest(name = "setName with value [{0}]")
        @ValueSource(strings = {"LHR", "CDG", "DXB", "SYD"})
        @DisplayName("setName accepts various IATA-style names")
        void setNameAcceptsVariousNames(String name) {
            airport.setName(name);
            assertEquals(name, airport.getName());
        }
    }

    // ---------------------------------------------------------------------------
    // Passenger management tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Passenger management tests")
    class PassengerManagementTests {

        @Test
        @DisplayName("getPassengers returns an unmodifiable list")
        void getPassengersReturnsUnmodifiableList() {
            List<Passenger> passengers = airport.getPassengers();
            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.add(new Passenger()));
        }

        @Test
        @DisplayName("addPassenger increases passenger count by one")
        void addPassengerIncreasesCount() {
            airport.addPassenger(new Passenger());
            assertEquals(1, airport.getPassengers().size());
        }

        @Test
        @DisplayName("addPassenger stores the correct passenger")
        void addPassengerStoresCorrectPassenger() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            assertTrue(airport.getPassengers().contains(passenger));
        }

        @Test
        @DisplayName("addPassenger called multiple times stores all passengers")
        void addPassengerMultipleTimesStoresAll() {
            var p1 = new Passenger();
            var p2 = new Passenger();
            var p3 = new Passenger();

            airport.addPassenger(p1);
            airport.addPassenger(p2);
            airport.addPassenger(p3);

            var passengers = airport.getPassengers();
            assertAll(
                    () -> assertEquals(3, passengers.size()),
                    () -> assertTrue(passengers.contains(p1)),
                    () -> assertTrue(passengers.contains(p2)),
                    () -> assertTrue(passengers.contains(p3))
            );
        }

        @Test
        @DisplayName("addPassenger accepts null without throwing")
        void addPassengerAcceptsNull() {
            assertDoesNotThrow(() -> airport.addPassenger(null));
            assertEquals(1, airport.getPassengers().size());
        }

        @Test
        @DisplayName("Same passenger can be added more than once")
        void samePassengerCanBeAddedMultipleTimes() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            airport.addPassenger(passenger);
            assertEquals(2, airport.getPassengers().size());
        }

        @Test
        @DisplayName("getPassengers returns empty list when no passenger added")
        void getPassengersReturnsEmptyListInitially() {
            assertTrue(airport.getPassengers().isEmpty());
        }

        @Test
        @DisplayName("getPassengers view reflects mutations made via addPassenger")
        void getPassengersViewReflectsMutations() {
            List<Passenger> snapshot1 = airport.getPassengers();
            airport.addPassenger(new Passenger());
            List<Passenger> snapshot2 = airport.getPassengers();

            // Both views should now show 1 passenger because they wrap the same backing list
            assertAll(
                    () -> assertEquals(1, snapshot1.size()),
                    () -> assertEquals(1, snapshot2.size())
            );
        }
    }
}