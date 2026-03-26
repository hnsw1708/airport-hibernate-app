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

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Parameterised constructor sets id and name correctly")
        void parameterisedConstructor_setsIdAndName() {
            var passenger = new Passenger(42, "Alice");

            assertAll(
                    () -> assertEquals(42, passenger.getId()),
                    () -> assertEquals("Alice", passenger.getName())
            );
        }

        @Test
        @DisplayName("No-args constructor creates passenger with default values")
        void noArgsConstructor_createsPassengerWithDefaults() {
            var passenger = new Passenger();

            assertAll(
                    () -> assertEquals(0, passenger.getId()),
                    () -> assertNull(passenger.getName()),
                    () -> assertNull(passenger.getAirport()),
                    () -> assertNotNull(passenger.getTickets()),
                    () -> assertTrue(passenger.getTickets().isEmpty())
            );
        }

        @Test
        @DisplayName("Parameterised constructor leaves airport null")
        void parameterisedConstructor_leavesAirportNull() {
            var passenger = new Passenger(1, "Bob");

            assertNull(passenger.getAirport());
        }

        @Test
        @DisplayName("Parameterised constructor initialises empty tickets list")
        void parameterisedConstructor_initialiesEmptyTicketsList() {
            var passenger = new Passenger(1, "Bob");

            assertNotNull(passenger.getTickets());
            assertTrue(passenger.getTickets().isEmpty());
        }
    }

    // -------------------------------------------------------------------------
    // Getters / Setters – id
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Id getter/setter tests")
    class IdTests {

        @Test
        @DisplayName("setId updates the id returned by getId")
        void setId_updatesId() {
            var passenger = new Passenger();
            passenger.setId(99);

            assertEquals(99, passenger.getId());
        }

        @Test
        @DisplayName("setId with zero is accepted")
        void setId_withZero() {
            var passenger = new Passenger(5, "Carol");
            passenger.setId(0);

            assertEquals(0, passenger.getId());
        }

        @Test
        @DisplayName("setId with negative value is accepted (no domain restriction)")
        void setId_withNegativeValue() {
            var passenger = new Passenger();
            passenger.setId(-1);

            assertEquals(-1, passenger.getId());
        }
    }

    // -------------------------------------------------------------------------
    // Getters / Setters – name
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Name getter/setter tests")
    class NameTests {

        @Test
        @DisplayName("setName updates the name returned by getName")
        void setName_updatesName() {
            var passenger = new Passenger();
            passenger.setName("Dave");

            assertEquals("Dave", passenger.getName());
        }

        @Test
        @DisplayName("setName accepts null")
        void setName_acceptsNull() {
            var passenger = new Passenger(1, "Eve");
            passenger.setName(null);

            assertNull(passenger.getName());
        }

        @Test
        @DisplayName("setName accepts empty string")
        void setName_acceptsEmptyString() {
            var passenger = new Passenger(1, "Frank");
            passenger.setName("");

            assertEquals("", passenger.getName());
        }

        @Test
        @DisplayName("setName accepts a long name (Java 21 text block used for readability)")
        void setName_acceptsLongName() {
            // Java 21 text block for a multi-word name definition
            String longName = """
                    Maximilian Alexander von Hohenstaufen-Wittelsbach""".strip();
            var passenger = new Passenger();
            passenger.setName(longName);

            assertEquals(longName, passenger.getName());
        }
    }

    // -------------------------------------------------------------------------
    // Getters / Setters – airport
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Airport getter/setter tests")
    class AirportTests {

        @Test
        @DisplayName("setAirport stores airport and getAirport returns it")
        void setAirport_storesAirport() {
            var passenger = new Passenger(1, "Grace");
            var airport = new Airport();
            passenger.setAirport(airport);

            assertSame(airport, passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport(null) clears the airport association")
        void setAirport_null_clearsAssociation() {
            var airport = new Airport();
            var passenger = new Passenger(1, "Hank");
            passenger.setAirport(airport);
            passenger.setAirport(null);

            assertNull(passenger.getAirport());
        }

        @Test
        @DisplayName("Setting a different airport overwrites the previous one")
        void setAirport_overwrites_previousAirport() {
            var passenger = new Passenger(1, "Iris");
            var firstAirport = new Airport();
            var secondAirport = new Airport();

            passenger.setAirport(firstAirport);
            passenger.setAirport(secondAirport);

            assertSame(secondAirport, passenger.getAirport());
        }
    }

    // -------------------------------------------------------------------------
    // Tickets – addTicket / getTickets
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Ticket management tests")
    class TicketTests {

        @Test
        @DisplayName("addTicket increases ticket list size by one")
        void addTicket_increasesListSize() {
            var passenger = new Passenger(1, "Jack");
            var ticket = new Ticket();

            passenger.addTicket(ticket);

            assertEquals(1, passenger.getTickets().size());
        }

        @Test
        @DisplayName("addTicket stores the correct ticket instance")
        void addTicket_storesCorrectTicket() {
            var passenger = new Passenger(1, "Karen");
            var ticket = new Ticket();

            passenger.addTicket(ticket);

            assertSame(ticket, passenger.getTickets().get(0));
        }

        @Test
        @DisplayName("Multiple addTicket calls preserve insertion order")
        void addTicket_multipleTickets_preservesOrder() {
            var passenger = new Passenger(1, "Leo");
            var t1 = new Ticket();
            var t2 = new Ticket();
            var t3 = new Ticket();

            passenger.addTicket(t1);
            passenger.addTicket(t2);
            passenger.addTicket(t3);

            List<Ticket> tickets = passenger.getTickets();
            assertAll(
                    () -> assertEquals(3, tickets.size()),
                    () -> assertSame(t1, tickets.get(0)),
                    () -> assertSame(t2, tickets.get(1)),
                    () -> assertSame(t3, tickets.get(2))
            );
        }

        @Test
        @DisplayName("getTickets returns empty list when no tickets added")
        void getTickets_emptyByDefault() {
            var passenger = new Passenger(1, "Mia");

            assertNotNull(passenger.getTickets());
            assertTrue(passenger.getTickets().isEmpty());
        }
    }
}