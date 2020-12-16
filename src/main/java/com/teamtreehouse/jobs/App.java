package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

  public static void main(String[] args) {
    JobService service = new JobService();
    boolean shouldRefresh = false;
    try {
      if (shouldRefresh) {
        service.refresh();
      }
      List<Job> jobs = service.loadJobs();
      System.out.printf("Total jobs:  %d %n %n", jobs.size());
      explore(jobs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void explore(List<Job> jobs) {

    getSnippetWordCount(jobs)
            .forEach((k, v) -> System.out.printf("'%s' occurs %d times %n", k, v));

  }

  //  This function shows how many times a word appears in our listing!
  // "This Is A Job" -> "This" "Is" "A" "Job" -> "this" "is" "a" "job" ->  "this" 1   "is" 1   "a" 1   "job" 1
  public static Map<String, Long> getSnippetWordCount(List<Job> jobs) {
    return jobs.stream()
            .map(Job::getSnippet) // we call a function to transform our job listing into a snippet.
            .map(snippet -> snippet.split("\\W+")) // we take the snippet and split it into an array
            .flatMap(Stream::of) // we stream within the stream and take in each word from our above array
            .filter(word -> word.length() > 0) // filter out anything without length
            .map(String::toLowerCase) // turn all of the words to lowercase for consistency
            .collect(Collectors.groupingBy(
                    word->word, //Or you can call Function.identity() instead of word->word
                    Collectors.counting()
            ));
  }

  //DECLARATIVE
  private static List<Job> getThreeJuniorJobsStream(List<Job> jobs) {
    return jobs.stream()
            .filter(App::isJuniorJob)      //Intermediary operation. Will run our method with method reference call.
            .limit(3)                      //stateful intermediary method, short-circuiting.
            .collect(Collectors.toList()); //return our 3 items as a list.

    //ArrayList<Something> something = new ArrayList<>();
    //you can also use forEach(something::add).... this will add all of the items to arrayList.
  }

  private static boolean isJuniorJob(Job job) {
      String title = job.getTitle().toLowerCase();
      return title.contains("junior") || title.contains("jr");
  }

  //IMPERATIVE
  private static List<Job> getThreeJuniorJobsImp(List<Job> jobs) {
    List<Job> juniorJobs = new ArrayList<>();

    for(Job job : jobs) {
      if(isJuniorJob(job)) {
        juniorJobs.add(job);
        if(juniorJobs.size() >= 3) break;
      }
    }

    return juniorJobs;
  }

  private static List<String> getCaptionStream(List<Job> jobs) {
    return jobs.stream()
            .filter(App::isJuniorJob)
            .map(Job::getCaption)   // Map allows us to take in a type and return another type - String in this example.
            .limit(3)
            .collect(Collectors.toList());

    //ArrayList<Something> something = new ArrayList<>();
    //you can also use forEach(something::add).... this will add all of the items to arrayList.
    // .map(Job::getCaption) is Method inference - We arent passing a value into this function
    // the method reference knows that we are using an instance of type job and that it can call
    // this function on it.
  }


  private static List<String> getCaptionsImp(List<Job> jobs) {
    List<String> captions = new ArrayList<>();
    for(Job job : jobs) {
      if(isJuniorJob(job)) {
        captions.add(job.getCaption());
        if(captions.size() >= 3) break;
      }
    }
    return captions;
  }

  private static void printJobStreams(List<Job> jobs) {
    // Think of streams like a pipeline - if state != DE it will be discarded.
    // If state = DE it moves to next filter. If City != Wilmington it will be discarded.
    // If both state and city match- it passes through and ends up in our results.
    jobs.stream()
            .filter(job -> job.getState().equals("PA")) //Intermediate Operation (Can have as many as you need!)
            .filter(job -> job.getCity().equals("Philadelphia")) //Intermediate Operation
            .forEach(System.out::println); //Terminal Operation with Method Reference to System.out::println method.
  }

  private static void printWilmingtonJobsImperatively(List<Job> jobs) {
    for(Job job : jobs) {
      if(job.getState().equals("DE") && job.getCity().equals("Wilmington")) {
        System.out.println(job);
      }
    }
  }
}
