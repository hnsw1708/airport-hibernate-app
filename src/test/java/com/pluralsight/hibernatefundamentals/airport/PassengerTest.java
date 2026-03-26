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

@DisplayName("Passenger entity tests")
class PassengerTest {

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger(1, "Alice");
    }

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Construction")
    class ConstructionTests {

        @Test
        @DisplayName("Parameterised constructor sets id and name correctly")
        void parameterisedConstructor_setsIdAndName() {
            var p = new Passenger(42, "Bob");
            assertAll(
                    () -> assertEquals(42, p.getId()),
                    () -> assertEquals("Bob", p.getName())
            );
        }

        @Test
        @DisplayName("No-arg constructor creates passenger with default values")
        void noArgConstructor_createsPassengerWithDefaults() {
            var p = new Passenger();
            assertAll(
                    () -> assertEquals(0, p.getId()),
                    () -> assertNull(p.getName()),
                    () -> assertNull(p.getAirport()),
                    () -> assertNotNull(p.getTickets()),
                    () -> assertTrue(p.getTickets().isEmpty())
            );
        }

        @Test
        @DisplayName("No-arg constructor initialises tickets list as empty")
        void noArgConstructor_ticketsListIsEmpty() {
            var p = new Passenger();
            assertTrue(p.getTickets().isEmpty());
        }

        @Test
        @DisplayName("Parameterised constructor initialises tickets list as empty")
        void parameterisedConstructor_ticketsListIsEmpty() {
            var p = new Passenger(1, "Carol");
            assertTrue(p.getTickets().isEmpty());
        }
    }

    // ------------------------------------------------------------------
    // Id getter / setter
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Id field")
    class IdTests {

        @Test
        @DisplayName("getId returns id set by constructor")
        void getId_returnsConstructorValue() {
            assertEquals(1, passenger.getId());
        }

        @Test
        @DisplayName("setId updates id")
        void setId_updatesId() {
            passenger.setId(99);
            assertEquals(99, passenger.getId());
        }

        @Test
        @DisplayName("setId accepts zero")
        void setId_acceptsZero() {
            passenger.setId(0);
            assertEquals(0, passenger.getId());
        }

        @Test
        @DisplayName("setId accepts negative value")
        void setId_acceptsNegativeValue() {
            passenger.setId(-5);
            assertEquals(-5, passenger.getId());
        }
    }

    // ------------------------------------------------------------------
    // Name getter / setter
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Name field")
    class NameTests {

        @Test
        @DisplayName("getName returns name set by constructor")
        void getName_returnsConstructorValue() {
            assertEquals("Alice", passenger.getName());
        }

        @Test
        @DisplayName("setName updates name")
        void setName_updatesName() {
            passenger.setName("David");
            assertEquals("David", passenger.getName());
        }

        @ParameterizedTest(name = "setName accepts [{0}]")
        @NullSource
        @DisplayName("setName accepts null")
        void setName_acceptsNull(String name) {
            passenger.setName(name);
            assertNull(passenger.getName());
        }

        @Test
        @DisplayName("setName accepts empty string")
        void setName_acceptsEmptyString() {
            passenger.setName("");
            assertEquals("", passenger.getName());
        }

        @Test
        @DisplayName("setName accepts name with special characters (text block example)")
        void setName_acceptsSpecialCharacters() {
            String specialName = """
                    O'Brien-Smith""".strip();
            passenger.setName(specialName);
            assertEquals("O'Brien-Smith", passenger.getName());
        }
    }

    // ------------------------------------------------------------------
    // Airport getter / setter
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Airport association")
    class AirportTests {

        @Test
        @DisplayName("getAirport returns null by default")
        void getAirport_returnsNullByDefault() {
            assertNull(passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport stores airport reference")
        void setAirport_storesReference() {
            Airport airport = new Airport(10, "Heathrow");
            passenger.setAirport(airport);
            assertSame(airport, passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport accepts null (dissociation)")
        void setAirport_acceptsNull() {
            Airport airport = new Airport(10, "Heathrow");
            passenger.setAirport(airport);
            passenger.setAirport(null);
            assertNull(passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport can replace existing airport")
        void setAirport_canReplaceExisting() {
            Airport first  = new Airport(1, "Heathrow");
            Airport second = new Airport(2, "Gatwick");
            passenger.setAirport(first);
            passenger.setAirport(second);
            assertSame(second, passenger.getAirport());
        }
    }

    // ------------------------------------------------------------------
    // Tickets collection
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Tickets collection")
    class TicketsTests {

        @Test
        @DisplayName("getTickets returns empty unmodifiable list initially")
        void getTickets_returnsEmptyListInitially() {
            List<Ticket> tickets = passenger.getTickets();
            assertNotNull(tickets);
            assertTrue(tickets.isEmpty());
        }

        @Test
        @DisplayName("addTicket adds a ticket that is visible via getTickets")
        void addTicket_addsTicketToList() {
            Ticket ticket = new Ticket(1, "250.0");
            passenger.addTicket(ticket);

            List<Ticket> tickets = passenger.getTickets();
            assertEquals(1, tickets.size());
            assertSame(ticket, tickets.get(0));
        }

        @Test
        @DisplayName("addTicket multiple tickets are all stored")
        void addTicket_multipleTicketsAreAllStored() {
            Ticket t1 = new Ticket(1, "100.0");
            Ticket t2 = new Ticket(2, "200.0");
            Ticket t3 = new Ticket(3, "300.0");

            passenger.addTicket(t1);
            passenger.addTicket(t2);
            passenger.addTicket(t3);

            assertEquals(3, passenger.getTickets().size());
        }

        @Test
        @DisplayName("getTickets returns an unmodifiable view - add throws UnsupportedOperationException")
        void getTickets_isUnmodifiable_addThrows() {
            Ticket ticket = new Ticket(1, "150.0");
            passenger.addTicket(ticket);

            List<Ticket> view = passenger.getTickets();
            assertThrows(UnsupportedOperationException.class,
                    () -> view.add(new Ticket(2, "999.0")));
        }

        @Test
        @DisplayName("getTickets returns an unmodifiable view - remove throws UnsupportedOperationException")
        void getTickets_isUnmodifiable_removeThrows() {
            Ticket ticket = new Ticket(1, "150.0");
            passenger.addTicket(ticket);

            List<Ticket> view = passenger.getTickets();
            assertThrows(UnsupportedOperationException.class,
                    () -> view.remove(0));
        }
    }
}