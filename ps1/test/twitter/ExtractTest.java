/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.Collections;

import org.junit.Test;

//package twitter;
//
//import static org.junit.Assert.*;
//
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Set;
//
//import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@alice @bob let's meet!", d3);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "@ALICE @Alice", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Tests for getTimespan()

    @Test 
    public void testGetTimespanSingleTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("Expected start time to be the same as end time for single tweet", d1, timespan.getStart());
        assertEquals("Expected start time to be the same as end time for single tweet", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("Expected earliest timestamp as start time", d1, timespan.getStart());
        assertEquals("Expected latest timestamp as end time", d3, timespan.getEnd());
    }

    @Test
    public void testGetTimespanSameTimestamp() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet4));
        assertEquals("Expected same start and end time for same timestamps", d2, timespan.getStart());
        assertEquals("Expected same start and end time for same timestamps", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanNoTweets() {
        Timespan timespan = Extract.getTimespan(Collections.emptyList());
        assertNull("Expected null for no tweets", timespan);
    }

    // Tests for getMentionedUsers()

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("Expected no mentioned users", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertEquals("Expected two mentioned users", 2, mentionedUsers.size());
        assertTrue("Expected mentioned user alice", mentionedUsers.contains("alice"));
        assertTrue("Expected mentioned user bob", mentionedUsers.contains("bob"));
    }

    @Test
    public void testGetMentionedUsersRepeatedMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertEquals("Expected one unique mentioned user", 1, mentionedUsers.size());
        assertTrue("Expected mentioned user alice", mentionedUsers.contains("alice"));
    }
    
    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertTrue("Expected mentioned user alice in a case-insensitive manner", mentionedUsers.contains("alice"));
    }
}