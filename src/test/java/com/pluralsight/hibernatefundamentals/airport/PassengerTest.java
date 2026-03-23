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

@DisplayName("Passenger Entity Tests")
class PassengerTest {

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger(1, "Alice Smith");
    }

    // ---------------------------------------------------------------------------
    // Constructor tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Constructor behaviour")
    class ConstructorTests {

        @Test
        @DisplayName("Parameterised constructor sets id and name")
        void parameterisedConstructor_setsIdAndName() {
            var p = new Passenger(42, "Bob Jones");

            assertAll(
                    () -> assertEquals(42, p.getId()),
                    () -> assertEquals("Bob Jones", p.getName())
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
        @DisplayName("Parameterised constructor initialises empty ticket list")
        void parameterisedConstructor_initialiseEmptyTicketList() {
            var p = new Passenger(1, "Charlie");

            assertNotNull(p.getTickets());
            assertTrue(p.getTickets().isEmpty());
        }
    }

    // ---------------------------------------------------------------------------
    // Getter / Setter tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Getter and Setter behaviour")
    class GetterSetterTests {

        @Test
        @DisplayName("setId / getId round-trip")
        void setId_andGetId_roundTrip() {
            passenger.setId(99);
            assertEquals(99, passenger.getId());
        }

        @Test
        @DisplayName("setName / getName round-trip")
        void setName_andGetName_roundTrip() {
            passenger.setName("Diana Prince");
            assertEquals("Diana Prince", passenger.getName());
        }

        @Test
        @DisplayName("setName accepts null")
        void setName_acceptsNull() {
            passenger.setName(null);
            assertNull(passenger.getName());
        }

        @ParameterizedTest(name = "setName accepts blank/empty: [{0}]")
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        @DisplayName("setName accepts null, empty and blank strings")
        void setName_acceptsNullEmptyAndBlank(String name) {
            assertDoesNotThrow(() -> passenger.setName(name));
            assertEquals(name, passenger.getName());
        }

        @Test
        @DisplayName("setId accepts zero")
        void setId_acceptsZero() {
            passenger.setId(0);
            assertEquals(0, passenger.getId());
        }

        @Test
        @DisplayName("setId accepts negative values")
        void setId_acceptsNegativeValues() {
            passenger.setId(-5);
            assertEquals(-5, passenger.getId());
        }

        @Test
        @DisplayName("setAirport / getAirport round-trip")
        void setAirport_andGetAirport_roundTrip() {
            var airport = new Airport(10, "Heathrow");
            passenger.setAirport(airport);

            assertSame(airport, passenger.getAirport());
        }

        @Test
        @DisplayName("setAirport accepts null")
        void setAirport_acceptsNull() {
            passenger.setAirport(new Airport(1, "LHR"));
            passenger.setAirport(null);
            assertNull(passenger.getAirport());
        }
    }

    // ---------------------------------------------------------------------------
    // Ticket management tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Ticket management")
    class TicketManagementTests {

        @Test
        @DisplayName("addTicket appends ticket to list")
        void addTicket_appendsTicketToList() {
            var ticket = new Ticket(101, "NYC");
            passenger.addTicket(ticket);

            List<Ticket> tickets = passenger.getTickets();
            assertEquals(1, tickets.size());
            assertSame(ticket, tickets.get(0));
        }

        @Test
        @DisplayName("addTicket multiple tickets preserves insertion order")
        void addTicket_multipleTickets_preservesOrder() {
            var t1 = new Ticket(1, "NYC");
            var t2 = new Ticket(2, "LAX");
            var t3 = new Ticket(3, "LHR");

            passenger.addTicket(t1);
            passenger.addTicket(t2);
            passenger.addTicket(t3);

            var tickets = passenger.getTickets();
            assertAll(
                    () -> assertEquals(3, tickets.size()),
                    () -> assertSame(t1, tickets.get(0)),
                    () -> assertSame(t2, tickets.get(1)),
                    () -> assertSame(t3, tickets.get(2))
            );
        }

        @Test
        @DisplayName("getTickets returns an unmodifiable view")
        void getTickets_returnsUnmodifiableList() {
            var ticket = new Ticket(1, "NYC");
            passenger.addTicket(ticket);

            List<Ticket> tickets = passenger.getTickets();
            assertThrows(UnsupportedOperationException.class,
                    () -> tickets.add(new Ticket(2, "LAX")));
        }

        @Test
        @DisplayName("getTickets remove throws UnsupportedOperationException")
        void getTickets_removeThrowsUnsupportedOperationException() {
            passenger.addTicket(new Ticket(1, "NYC"));

            List<Ticket> tickets = passenger.getTickets();
            assertThrows(UnsupportedOperationException.class,
                    () -> tickets.remove(0));
        }

        @Test
        @DisplayName("getTickets returns empty unmodifiable list when no tickets added")
        void getTickets_emptyListIsUnmodifiable() {
            List<Ticket> tickets = passenger.getTickets();

            assertTrue(tickets.isEmpty());
            assertThrows(UnsupportedOperationException.class,
                    () -> tickets.add(new Ticket(1, "SFO")));
        }

        @Test
        @DisplayName("addTicket with null ticket — adds null entry without NPE")
        void addTicket_withNullTicket_doesNotThrow() {
            // The production code does not guard against null; verify current behaviour
            assertDoesNotThrow(() -> passenger.addTicket(null));
            assertEquals(1, passenger.getTickets().size());
            assertNull(passenger.getTickets().get(0));
        }
    }

    // ---------------------------------------------------------------------------
    // Airport association tests
    // ---------------------------------------------------------------------------
    @Nested
    @DisplayName("Airport association")
    class AirportAssociationTests {

        @Test
        @DisplayName("New passenger has no airport associated")
        void newPassenger_hasNoAirport() {
            assertNull(passenger.getAirport());
        }

        @Test
        @DisplayName("Passenger can be reassigned to a different airport")
        void passenger_canBeReassignedToAnotherAirport() {
            var lhr = new Airport(1, "Heathrow");
            var jfk = new Airport(2, "JFK");

            passenger.setAirport(lhr);
            passenger.setAirport(jfk);

            assertSame(jfk, passenger.getAirport());
        }
    }

    // ---------------------------------------------------------------------------
    // Java 21 record-based helper — verifies data captured via getters
    // ---------------------------------------------------------------------------
    @Test
    @DisplayName("Passenger state can be captured in a Java 21 record")
    void passengerState_capturedInRecord() {
        record PassengerSnapshot(int id, String name) {}

        passenger.setId(7);
        passenger.setName("Eve");

        var snapshot = new PassengerSnapshot(passenger.getId(), passenger.getName());

        assertAll(
                () -> assertEquals(7, snapshot.id()),
                () -> assertEquals("Eve", snapshot.name())
        );
    }

    @Test
    @DisplayName("Pattern matching instanceof works on Passenger")
    void patternMatching_instanceof_worksOnPassenger() {
        Object obj = passenger;

        assertTrue(obj instanceof Passenger p && p.getId() == 1);
    }

    @Test
    @DisplayName("Text block can describe passenger information")
    void textBlock_describesPassengerInfo() {
        passenger.setName("Frank Castle");
        passenger.setId(3);

        String expected = """
                id=3, name=Frank Castle
                """;

        String actual = "id=%d, name=%s%n".formatted(passenger.getId(), passenger.getName());
        assertEquals(expected, actual);
    }
}
