package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import java.io.IOException;
import java.util.List;

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

    // Think of streams like a pipeline - if state != DE it will be discarded.
    // If state = DE it moves to next filter. If City != Wilmington it will be discarded.
    // If both state and city match- it passes through and ends up in our results.
    jobs.stream()
            .filter(job -> job.getState().equals("DE")) //Intermediate Operation (Can have as many as you need!)
            .filter(job -> job.getCity().equals("Wilmington")) //Intermediate Operation
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
