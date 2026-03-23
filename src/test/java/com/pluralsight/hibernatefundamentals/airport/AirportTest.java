package com.pluralsight.hibernatefundamentals.airport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    // Constructors
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Constructor Tests")
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
        @DisplayName("No-arg constructor leaves id at default and name null")
        void noArgConstructor_defaultValues() {
            var ap = new Airport();

            assertAll(
                    () -> assertEquals(0, ap.getId()),
                    () -> assertNull(ap.getName())
            );
        }

        @Test
        @DisplayName("No-arg constructor initialises passengers list as empty")
        void noArgConstructor_passengersListIsEmpty() {
            var ap = new Airport();

            assertNotNull(ap.getPassengers());
            assertTrue(ap.getPassengers().isEmpty());
        }

        @Test
        @DisplayName("Parameterised constructor initialises passengers list as empty")
        void parameterisedConstructor_passengersListIsEmpty() {
            assertNotNull(airport.getPassengers());
            assertTrue(airport.getPassengers().isEmpty());
        }
    }

    // ---------------------------------------------------------------------------
    // getId / setId
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("id field Tests")
    class IdTests {

        @Test
        @DisplayName("getId returns value set via constructor")
        void getId_returnsConstructorValue() {
            assertEquals(1, airport.getId());
        }

        @Test
        @DisplayName("setId updates the id")
        void setId_updatesId() {
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
        @DisplayName("setId accepts negative values")
        void setId_acceptsNegativeValue() {
            airport.setId(-5);
            assertEquals(-5, airport.getId());
        }

        @ParameterizedTest(name = "setId({0})")
        @ValueSource(ints = {1, 100, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("setId stores any int value correctly")
        void setId_storesAnyIntValue(int value) {
            airport.setId(value);
            assertEquals(value, airport.getId());
        }
    }

    // ---------------------------------------------------------------------------
    // getName / setName
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("name field Tests")
    class NameTests {

        @Test
        @DisplayName("getName returns value set via constructor")
        void getName_returnsConstructorValue() {
            assertEquals("Heathrow", airport.getName());
        }

        @Test
        @DisplayName("setName updates the name")
        void setName_updatesName() {
            airport.setName("Gatwick");
            assertEquals("Gatwick", airport.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("setName accepts null and empty string")
        void setName_acceptsNullAndEmpty(String value) {
            airport.setName(value);
            assertEquals(value, airport.getName());
        }

        @Test
        @DisplayName("setName accepts a name with special characters")
        void setName_acceptsSpecialCharacters() {
            var specialName = "São Paulo–Guarulhos";
            airport.setName(specialName);
            assertEquals(specialName, airport.getName());
        }

        @Test
        @DisplayName("setName accepts a long name (text block illustration)")
        void setName_acceptsLongName() {
            var longName = """
                    Very Long International Airport Name That Exceeds Normal Length\
                    """.strip();
            airport.setName(longName);
            assertEquals(longName, airport.getName());
        }
    }

    // ---------------------------------------------------------------------------
    // addPassenger / getPassengers
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Passenger Management Tests")
    class PassengerTests {

        @Test
        @DisplayName("getPassengers returns empty unmodifiable list initially")
        void getPassengers_initiallyEmpty() {
            assertTrue(airport.getPassengers().isEmpty());
        }

        @Test
        @DisplayName("addPassenger increases passengers list size")
        void addPassenger_increasesList() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            assertEquals(1, airport.getPassengers().size());
        }

        @Test
        @DisplayName("addPassenger stores the correct passenger instance")
        void addPassenger_storesCorrectInstance() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);

            assertSame(passenger, airport.getPassengers().get(0));
        }

        @Test
        @DisplayName("Multiple passengers are all stored in order")
        void addPassenger_multiplePassengersStoredInOrder() {
            var p1 = new Passenger();
            var p2 = new Passenger();
            var p3 = new Passenger();

            airport.addPassenger(p1);
            airport.addPassenger(p2);
            airport.addPassenger(p3);

            List<Passenger> passengers = airport.getPassengers();
            assertAll(
                    () -> assertEquals(3, passengers.size()),
                    () -> assertSame(p1, passengers.get(0)),
                    () -> assertSame(p2, passengers.get(1)),
                    () -> assertSame(p3, passengers.get(2))
            );
        }

        @Test
        @DisplayName("getPassengers returns an unmodifiable list — add throws")
        void getPassengers_isUnmodifiable_addThrows() {
            var returnedList = airport.getPassengers();

            assertThrows(UnsupportedOperationException.class,
                    () -> returnedList.add(new Passenger()));
        }

        @Test
        @DisplayName("getPassengers returns an unmodifiable list — remove throws")
        void getPassengers_isUnmodifiable_removeThrows() {
            airport.addPassenger(new Passenger());
            var returnedList = airport.getPassengers();

            assertThrows(UnsupportedOperationException.class,
                    () -> returnedList.remove(0));
        }

        @Test
        @DisplayName("Adding the same passenger twice results in two entries")
        void addPassenger_sameInstanceTwice_appearsInListTwice() {
            var passenger = new Passenger();
            airport.addPassenger(passenger);
            airport.addPassenger(passenger);

            assertEquals(2, airport.getPassengers().size());
        }

        @Test
        @DisplayName("Pattern matching — getPassengers returns a List")
        void getPassengers_patternMatching_isList() {
            Object result = airport.getPassengers();

            if (result instanceof List<?> list) {
                assertTrue(list.isEmpty());
            } else {
                fail("Expected a List but got: " + result.getClass());
            }
        }
    }

    // ---------------------------------------------------------------------------
    // Full round-trip / integration-style
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Round-trip / Combined Behaviour Tests")
    class RoundTripTests {

        @Test
        @DisplayName("State is consistent after multiple setters and addPassenger calls")
        void stateConsistentAfterMutations() {
            airport.setId(7);
            airport.setName("Charles de Gaulle");
            airport.addPassenger(new Passenger());
            airport.addPassenger(new Passenger());

            assertAll(
                    () -> assertEquals(7, airport.getId()),
                    () -> assertEquals("Charles de Gaulle", airport.getName()),
                    () -> assertEquals(2, airport.getPassengers().size())
            );
        }

        @Test
        @DisplayName("No-arg constructor followed by setters behaves same as parameterised constructor")
        void noArgThenSetters_equivalentToParameterisedConstructor() {
            var fromNoArg = new Airport();
            fromNoArg.setId(1);
            fromNoArg.setName("Heathrow");

            assertAll(
                    () -> assertEquals(airport.getId(), fromNoArg.getId()),
                    () -> assertEquals(airport.getName(), fromNoArg.getName()),
                    () -> assertEquals(airport.getPassengers().size(), fromNoArg.getPassengers().size())
            );
        }
    }
}
