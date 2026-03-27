package com.pluralsight.hibernatefundamentals.airport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passenger entity tests")
class PassengerTest {

    // ---------------------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("No-arg constructor creates passenger with default values")
        void noArgConstructor_shouldCreatePassengerWithDefaultValues() {
            Passenger passenger = new Passenger();

            assertEquals(0, passenger.getId());
            assertNull(passenger.getName());
            assertNull(passenger.getAirport());
            assertNotNull(passenger.getTickets());
            assertTrue(passenger.getTickets().isEmpty());
        }

        @Test
        @DisplayName("Parameterised constructor sets id and name correctly")
        void parameterisedConstructor_shouldSetIdAndName() {
            Passenger passenger = new Passenger(42, "Alice");

            assertEquals(42, passenger.getId());
            assertEquals("Alice", passenger.getName());
        }

        @Test
        @DisplayName("Parameterised constructor initialises empty ticket list")
        void parameterisedConstructor_shouldInitialiseEmptyTicketList() {
            Passenger passenger = new Passenger(1, "Bob");

            assertNotNull(passenger.getTickets());
            assertTrue(passenger.getTickets().isEmpty());
        }

        @Test
        @DisplayName("Parameterised constructor leaves airport null")
        void parameterisedConstructor_shouldLeaveAirportNull() {
            Passenger passenger = new Passenger(1, "Carol");

            assertNull(passenger.getAirport());
        }
    }

    // ---------------------------------------------------------------------------
    // getId / setId
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Id property tests")
    class IdTests {

        @Test
        @DisplayName("setId updates id returned by getId")
        void setId_shouldUpdateId() {
            Passenger passenger = new Passenger();
            passenger.setId(99);

            assertEquals(99, passenger.getId());
        }

        @Test
        @DisplayName("setId with zero is accepted")
        void setId_withZero_shouldBeAccepted() {
            Passenger passenger = new Passenger(5, "Dave");
            passenger.setId(0);

            assertEquals(0, passenger.getId());
        }

        @Test
        @DisplayName("setId with negative value is accepted")
        void setId_withNegativeValue_shouldBeAccepted() {
            Passenger passenger = new Passenger();
            passenger.setId(-1);

            assertEquals(-1, passenger.getId());
        }
    }

    // ---------------------------------------------------------------------------
    // getName / setName
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Name property tests")
    class NameTests {

        @Test
        @DisplayName("setName updates name returned by getName")
        void setName_shouldUpdateName() {
            Passenger passenger = new Passenger();
            passenger.setName("Eve");

            assertEquals("Eve", passenger.getName());
        }

        @Test
        @DisplayName("setName with null is accepted")
        void setName_withNull_shouldBeAccepted() {
            Passenger passenger = new Passenger(1, "Frank");
            passenger.setName(null);

            assertNull(passenger.getName());
        }

        @Test
        @DisplayName("setName with empty string is accepted")
        void setName_withEmptyString_shouldBeAccepted() {
            Passenger passenger = new Passenger();
            passenger.setName("");

            assertEquals("", passenger.getName());
        }

        @Test
        @DisplayName("setName with text block value is accepted")
        void setName_withTextBlockValue_shouldBeAccepted() {
            // Java 21 text block used for multi-word name
            String fullName = """
                    John Michael Doe""".strip();

            Passenger passenger = new Passenger();
            passenger.setName(fullName);

            assertEquals("John Michael Doe", passenger.getName());
        }
    }

    // ---------------------------------------------------------------------------
    // getAirport / setAirport
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Airport property tests")
    class AirportTests {

        @Test
        @DisplayName("setAirport updates airport returned by getAirport")
        void setAirport_shouldUpdateAirport() {
            Passenger passenger = new Passenger(1, "Grace");
            Airport airport = new Airport();
            passenger.setAirport(airport);

            assertSame(airport, passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport with null clears the airport")
        void setAirport_withNull_shouldClearAirport() {
            Passenger passenger = new Passenger(1, "Hank");
            Airport airport = new Airport();
            passenger.setAirport(airport);
            passenger.setAirport(null);

            assertNull(passenger.getAirport());
        }
    }

    // ---------------------------------------------------------------------------
    // addTicket / getTickets
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Ticket collection tests")
    class TicketTests {

        @Test
        @DisplayName("addTicket increases ticket list size by one")
        void addTicket_shouldIncreaseListSizeByOne() {
            Passenger passenger = new Passenger(1, "Ivy");
            Ticket ticket = new Ticket();

            passenger.addTicket(ticket);

            assertEquals(1, passenger.getTickets().size());
        }

        @Test
        @DisplayName("addTicket stores the ticket in the list")
        void addTicket_shouldStoreTicketInList() {
            Passenger passenger = new Passenger(1, "Jack");
            Ticket ticket = new Ticket();

            passenger.addTicket(ticket);

            assertTrue(passenger.getTickets().contains(ticket));
        }

        @Test
        @DisplayName("addTicket called multiple times accumulates all tickets")
        void addTicket_calledMultipleTimes_shouldAccumulateAllTickets() {
            Passenger passenger = new Passenger(1, "Kate");
            Ticket t1 = new Ticket();
            Ticket t2 = new Ticket();
            Ticket t3 = new Ticket();

            passenger.addTicket(t1);
            passenger.addTicket(t2);
            passenger.addTicket(t3);

            List<Ticket> tickets = passenger.getTickets();
            assertEquals(3, tickets.size());
            assertTrue(tickets.containsAll(List.of(t1, t2, t3)));
        }

        @Test
        @DisplayName("getTickets returns an unmodifiable view")
        void getTickets_shouldReturnUnmodifiableList() {
            Passenger passenger = new Passenger(1, "Leo");
            passenger.addTicket(new Ticket());

            List<Ticket> tickets = passenger.getTickets();

            assertThrows(UnsupportedOperationException.class,
                    () -> tickets.add(new Ticket()),
                    "getTickets() should return an unmodifiable list");
        }

        @Test
        @DisplayName("getTickets returns an unmodifiable view – remove also disallowed")
        void getTickets_shouldNotAllowRemove() {
            Passenger passenger = new Passenger(1, "Mia");
            Ticket ticket = new Ticket();
            passenger.addTicket(ticket);

            List<Ticket> tickets = passenger.getTickets();

            assertThrows(UnsupportedOperationException.class,
                    () -> tickets.remove(ticket),
                    "getTickets() should not allow remove()");
        }

        @Test
        @DisplayName("getTickets initially returns empty unmodifiable list for no-arg constructor")
        void getTickets_initiallyEmpty_forNoArgConstructor() {
            Passenger passenger = new Passenger();

            assertTrue(passenger.getTickets().isEmpty());
        }

        @Test
        @DisplayName("addTicket with null does not throw (null stored in list)")
        void addTicket_withNull_shouldNotThrow() {
            Passenger passenger = new Passenger(1, "Nina");

            assertDoesNotThrow(() -> passenger.addTicket(null));
            assertEquals(1, passenger.getTickets().size());
            assertNull(passenger.getTickets().get(0));
        }
    }

    // ---------------------------------------------------------------------------
    // Pattern-matching switch (Java 21) – illustrates using sealed-like dispatch
    // on Passenger state for documentation purposes
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Java 21 pattern-matching usage in tests")
    class PatternMatchingTests {

        @Test
        @DisplayName("instanceof pattern match correctly identifies Passenger type")
        void instanceofPatternMatch_shouldIdentifyPassenger() {
            Object obj = new Passenger(7, "Oscar");

            // Java 21 pattern matching for instanceof
            if (obj instanceof Passenger p) {
                assertEquals(7, p.getId());
                assertEquals("Oscar", p.getName());
            } else {
                fail("Object should be an instance of Passenger");
            }
        }

        @Test
        @DisplayName("Record snapshot of passenger data matches expected values")
        void recordSnapshot_shouldMatchPassengerData() {
            // Java 21 record used inside the test for a value snapshot
            record PassengerSnapshot(int id, String name) {}

            Passenger passenger = new Passenger(10, "Paula");
            PassengerSnapshot snapshot = new PassengerSnapshot(passenger.getId(), passenger.getName());

            assertEquals(new PassengerSnapshot(10, "Paula"), snapshot);
        }
    }
}
