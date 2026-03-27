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
        @DisplayName("Parameterised constructor sets id and name correctly")
        void parameterisedConstructor_setsIdAndName() {
            var ap = new Airport(42, "JFK");

            assertAll(
                    () -> assertEquals(42, ap.getId()),
                    () -> assertEquals("JFK", ap.getName())
            );
        }

        @Test
        @DisplayName("No-arg constructor creates Airport with default values")
        void noArgConstructor_createsAirportWithDefaults() {
            var ap = new Airport();

            assertAll(
                    () -> assertEquals(0, ap.getId()),
                    () -> assertNull(ap.getName()),
                    () -> assertNotNull(ap.getPassengers()),
                    () -> assertTrue(ap.getPassengers().isEmpty())
            );
        }

        @Test
        @DisplayName("Parameterised constructor initialises empty passengers list")
        void parameterisedConstructor_initialisesEmptyPassengersList() {
            var ap = new Airport(1, "CDG");

            assertNotNull(ap.getPassengers());
            assertTrue(ap.getPassengers().isEmpty());
        }
    }

    // ---------------------------------------------------------------------------
    // Getter / Setter tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Getter and Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("setId updates the id")
        void setId_updatesId() {
            airport.setId(99);
            assertEquals(99, airport.getId());
        }

        @Test
        @DisplayName("setName updates the name")
        void setName_updatesName() {
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
    }

    // ---------------------------------------------------------------------------
    // Passenger management tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Passenger management tests")
    class PassengerManagementTests {

        @Test
        @DisplayName("addPassenger adds a passenger to the list")
        void addPassenger_addsPassengerToList() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            List<Passenger> passengers = airport.getPassengers();
            assertEquals(1, passengers.size());
            assertTrue(passengers.contains(passenger));
        }

        @Test
        @DisplayName("addPassenger multiple times grows the list")
        void addPassenger_multipleTimes_growsList() {
            var p1 = new Passenger();
            var p2 = new Passenger();
            var p3 = new Passenger();

            airport.addPassenger(p1);
            airport.addPassenger(p2);
            airport.addPassenger(p3);

            assertEquals(3, airport.getPassengers().size());
        }

        @Test
        @DisplayName("addPassenger preserves insertion order")
        void addPassenger_preservesInsertionOrder() {
            var p1 = new Passenger();
            var p2 = new Passenger();

            airport.addPassenger(p1);
            airport.addPassenger(p2);

            var passengers = airport.getPassengers();
            assertAll(
                    () -> assertSame(p1, passengers.get(0)),
                    () -> assertSame(p2, passengers.get(1))
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
        void getPassengers_remove_throwsUnsupportedOperationException() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            List<Passenger> passengers = airport.getPassengers();

            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.remove(0));
        }

        @Test
        @DisplayName("getPassengers returns empty unmodifiable list when no passengers added")
        void getPassengers_noneAdded_returnsEmptyUnmodifiableList() {
            List<Passenger> passengers = airport.getPassengers();

            assertTrue(passengers.isEmpty());
            assertThrows(UnsupportedOperationException.class,
                    () -> passengers.add(new Passenger()));
        }

        @Test
        @DisplayName("addPassenger accepts null passenger without throwing")
        void addPassenger_nullPassenger_doesNotThrow() {
            assertDoesNotThrow(() -> airport.addPassenger(null));
            assertEquals(1, airport.getPassengers().size());
            assertNull(airport.getPassengers().get(0));
        }
    }

    // ---------------------------------------------------------------------------
    // Java 21 pattern-matching / text-block style assertions
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Java 21 feature demonstrations")
    class Java21FeatureTests {

        @Test
        @DisplayName("Airport name matches using pattern matching instanceof")
        void airportName_patternMatchingInstanceof() {
            Object name = airport.getName();

            if (name instanceof String s) {
                assertEquals("Heathrow", s);
            } else {
                fail("Name should be a String");
            }
        }

        @Test
        @DisplayName("Airport toString-like representation via text block comparison")
        void airportFields_textBlockVerification() {
            var expected = """
                    id=1
                    name=Heathrow
                    """;

            var actual = """
                    id=%d
                    name=%s
                    """.formatted(airport.getId(), airport.getName());

            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("Record-style snapshot of airport data is consistent")
        void airportRecord_snapshotIsConsistent() {
            record AirportSnapshot(int id, String name) {}

            var snapshot = new AirportSnapshot(airport.getId(), airport.getName());

            assertAll(
                    () -> assertEquals(1, snapshot.id()),
                    () -> assertEquals("Heathrow", snapshot.name())
            );
        }
    }

    // ---------------------------------------------------------------------------
    // Parameterised edge-case tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Parameterised name tests")
    class ParameterisedNameTests {

        @ParameterizedTest
        @NullSource
        @DisplayName("setName with null does not throw")
        void setName_null_doesNotThrow(String name) {
            assertDoesNotThrow(() -> airport.setName(name));
            assertNull(airport.getName());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "A", "Very Long Airport Name That Could Be A Valid Value"})
        @DisplayName("setName with various strings stores them correctly")
        void setName_variousStrings_storedCorrectly(String name) {
            airport.setName(name);
            assertEquals(name, airport.getName());
        }
    }
}
