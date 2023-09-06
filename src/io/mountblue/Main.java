package io.mountblue;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final int MATCH_ID = 0;
    private static final int MATCH_SEASON = 1;
    private static final int MATCH_CITY = 2;
    private static final int MATCH_DATE = 3;
    private static final int MATCH_TEAM1 = 4;
    private static final int MATCH_TEAM2 = 5;
    private static final int MATCH_TOSS_WINNER = 6;
    private static final int MATCH_TOSS_DECISION = 7;
    private static final int MATCH_RESULT = 8;
    private static final int MATCH_DL_APPLIED = 9;
    private static final int MATCH_WINNER = 10;
    private static final int MATCH_WIN_BY_RUNS = 11;
    private static final int MATCH_WIN_BY_WICKETS = 12;
    private static final int MATCH_PLAYER_OF_MATCH = 13;
    private static final int MATCH_VENUE = 14;
    private static final int MATCH_UMPIRE1 = 15;
    private static final int MATCH_UMPIRE2 = 16;
    private static final int MATCH_UMPIRE3 = 17;



    private static final int DELIVER_ID = 0;
    private static final int DELIVER_INNING = 1;
    private static final int DELIVER_BATTING_TEAM = 2;
    private static final int DELIVER_BOWLING_TEAM = 3;
    private static final int DELIVER_OVER = 4;
    private static final int DELIVER_BALL = 5;
    private static final int DELIVER_BATSMAN =6;
    private static final int DELIVER_NON_STRIKER = 7;
    private static final int DELIVER_BOWLER = 8;
    private static final int DELIVER_SUPER_OVER = 9;
    private static final int DELIVER_WIDE_RUNS = 10;
    private static final int DELIVER_BYE_RUNS = 11;
    private static final int DELIVER_LEG_BYE_RUNS = 12;
    private static final int DELIVER_NO_BALL_RUNS = 13;
    private static final int DELIVER_PENALTY_RUNS = 14;
    private static final int DELIVER_BATSMAN_RUNS = 15;
    private static final int DELIVER_EXTRA_RUNS = 16;
    private static final int DELIVER_TOTAL_RUNS = 17;
    private static final int DELIVER_PLAYER_DISMISSED = 18;
    private static final int DELIVER_DISMISSAL_KIND = 19;
    private static final int DELIVER_FIELDER = 20;






    public static void main(String[] args) throws IOException {

        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerYear(matches);
        findNumberOfMatchesWonByAllTeams(matches);
        findExtraRunsConcededByAllTeamsForParticularYear(matches, deliveries);
        findTopEconomicalBowlersForParticularYear(matches, deliveries);
    }

    private static void findBowlerWhoTookMostWicketsByTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        HashMap<String, Integer> totalWicketsOfEachBowler = new HashMap<>();
        HashMap<String, String> teamToWhichBowlerBelongs = new HashMap<>();
        for (Match match : matches) {
            if (match.getSeason().equals("2016")) {
                for (Delivery delivery : deliveries) {
                    if ((delivery.getId().equals(match.getId())) && (delivery.getDismissalKind() != null)) {
                        String bowler = delivery.getBowler();
                        String team = delivery.getBowlingTeam();
                        teamToWhichBowlerBelongs.put(bowler, team);
                        totalWicketsOfEachBowler.put(bowler, totalWicketsOfEachBowler.getOrDefault(bowler, 0) + 1);
                    }
                }
            }
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(totalWicketsOfEachBowler.entrySet());
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for (Map.Entry<String, Integer> values : entryList) {
            System.out.println(values.getKey() + "  " + values.getValue() + "  " + teamToWhichBowlerBelongs.get(values.getKey()));
        }
    }


    private static void findTopEconomicalBowlersForParticularYear(List<Match> matches, List<Delivery> deliveries) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the year : ");
        String year = sc.next();
        HashMap<String, Integer> runs = new HashMap<>();
        HashMap<String, Integer> ballsBowled = new HashMap<>();
        for(Match match : matches)
        {
            if(match.getSeason().equals(year))
            {
                for(Delivery delivery : deliveries)
                {
                    if(delivery.getId().equals(match.getId()))
                    {
                        String bowlerName = delivery.getBowler();
                        int runsConceded = Integer.parseInt(delivery.getTotalRuns());
                        runs.put(bowlerName, runs.getOrDefault(bowlerName, 0)+runsConceded);
                        ballsBowled.put(bowlerName, ballsBowled.getOrDefault(bowlerName, 0)+1);
                    }
                }
            }
        }
        HashMap<String, Double> economy = new HashMap<>();
        for(String bowler : ballsBowled.keySet())
        {
            int runsConceded = runs.get(bowler);
            int balls = ballsBowled.get(bowler);

            double economyRate = (double)runsConceded/(balls/6.0);

            economy.put(bowler, economyRate);
        }
        Map<String, Double> topTenEconomicalBowlers = economy.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("Top 10 economical bowlers in " + year + " are :");
        System.out.println(topTenEconomicalBowlers);
    }

    private static void findExtraRunsConcededByAllTeamsForParticularYear(List<Match> matches, List<Delivery> deliveries) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the year : ");
        String year = sc.next();
        HashMap<String, Integer> extraRunsConcededByAllTeams = new HashMap<>();
        for(Match match : matches)
        {
            if(match.getSeason().equals(year))
            {
                for(Delivery delivery : deliveries)
                {
                    if(delivery.getId().equals(match.getId()))
                    {
                        String bowlingTeam = delivery.getBowlingTeam();
                        int extraRuns = Integer.parseInt(delivery.getExtraRuns());
                        extraRunsConcededByAllTeams.put(bowlingTeam, extraRunsConcededByAllTeams.getOrDefault(bowlingTeam, 0)+extraRuns);
                    }
                }
            }
        }
        System.out.println("Extra runs conceded by all teams in " + year + " are :");
        System.out.println(extraRunsConcededByAllTeams);
    }

    private static void findNumberOfMatchesWonByAllTeams(List<Match> matches) {
        HashMap<String, Integer> matchesWonByAllTeams = new HashMap<>();
        for(Match match : matches)
        {
            String winner = match.getWinner();
            if(!winner.isEmpty())
                matchesWonByAllTeams.put(winner, matchesWonByAllTeams.getOrDefault(winner, 0)+1);
        }
        System.out.println("Number of matches won by all teams :");
        System.out.println(matchesWonByAllTeams);
    }

    private static void findNumberOfMatchesPlayedPerYear(List<Match> matches) {
        HashMap<String, Integer> matchesPlayedPerYear = new HashMap<>();
        for (Match match : matches) {
            String season = match.getSeason();
            matchesPlayedPerYear.put(season, matchesPlayedPerYear.getOrDefault(season, 0) + 1);
        }

        System.out.println("Number of matches played per year:");
        System.out.println(matchesPlayedPerYear);
    }

    private static List<Delivery> getDeliveriesData() throws IOException {
        List<Delivery> deliveries = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("deliveries.csv"));
        String line;
        int x=1;
        while ((line = reader.readLine()) != null) {
            if(x==1)
            {
                x=2;
                continue;
            }
            String[] data = line.split(",");

            Delivery deliver = new Delivery();
            deliver.setId(data[DELIVER_ID]);
            deliver.setInning(data[DELIVER_INNING]);
            deliver.setBattingTeam(data[DELIVER_BATTING_TEAM]);
            deliver.setBowlingTeam(data[DELIVER_BOWLING_TEAM]);
            deliver.setOver(data[DELIVER_OVER]);
            deliver.setBall(data[DELIVER_BALL]);
            deliver.setBatsman(data[DELIVER_BATSMAN]);
            deliver.setNonStriker(data[DELIVER_NON_STRIKER]);
            deliver.setBowler(data[DELIVER_BOWLER]);
            deliver.setSuperOver(data[DELIVER_SUPER_OVER]);
            deliver.setWideRuns(data[DELIVER_WIDE_RUNS]);
            deliver.setByeRuns(data[DELIVER_BYE_RUNS]);
            deliver.setLegByeRuns(data[DELIVER_LEG_BYE_RUNS]);
            deliver.setNoBallRuns(data[DELIVER_NO_BALL_RUNS]);
            deliver.setPenaltyRuns(data[DELIVER_PENALTY_RUNS]);
            deliver.setBatsmanRuns(data[DELIVER_BATSMAN_RUNS]);
            deliver.setExtraRuns(data[DELIVER_EXTRA_RUNS]);
            deliver.setTotalRuns(data[DELIVER_TOTAL_RUNS]);
            if(data.length-1 >= DELIVER_PLAYER_DISMISSED)
            {
                deliver.setPlayerDismissed(data[DELIVER_PLAYER_DISMISSED]);
            }
            if(data.length-1 >= DELIVER_DISMISSAL_KIND)
            {
                deliver.setDismissalKind(data[DELIVER_DISMISSAL_KIND]);
            }
            if(data.length-1 >= DELIVER_FIELDER)
            {
                deliver.setFielder(data[DELIVER_FIELDER]);
            }



            deliveries.add(deliver);
        }
        return deliveries;
    }

    private static List<Match> getMatchesData() throws IOException {
        List<Match> matches = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("matches.csv"));
        String line;
        int  x=1;
        while ((line = reader.readLine()) != null) {
            if(x==1)
            {
                x=2;
                continue;
            }
            String[] data = line.split(",");

            Match match = new Match();
            match.setId(data[MATCH_ID]);
            match.setSeason(data[MATCH_SEASON]);
            //System.out.println(match.getSeason());
            match.setCity(data[MATCH_CITY]);
            match.setDate(data[MATCH_DATE]);
            match.setTeam1(data[MATCH_TEAM1]);
            match.setTeam2(data[MATCH_TEAM2]);
            match.setTossWinner(data[MATCH_TOSS_WINNER]);
            match.setTossDecision(data[MATCH_TOSS_DECISION]);
            match.setResult(data[MATCH_RESULT]);
            match.setDlApplied(data[MATCH_DL_APPLIED]);
            match.setWinner(data[MATCH_WINNER]);
            match.setWinByRuns(data[MATCH_WIN_BY_RUNS]);
            match.setWinByWickets(data[MATCH_WIN_BY_WICKETS]);
            match.setPlayerOfMatch(data[MATCH_PLAYER_OF_MATCH]);
            match.setVenue(data[MATCH_VENUE]);
            if(data.length-1 >= MATCH_UMPIRE1) {
                match.setUmpire1(data[MATCH_UMPIRE1]);
            }
            if(data.length-1 >= MATCH_UMPIRE2) {
                match.setUmpire2(data[MATCH_UMPIRE2]);
            }

            matches.add(match);
        }
        return matches;
    }

}