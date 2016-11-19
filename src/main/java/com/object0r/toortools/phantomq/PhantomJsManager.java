package com.object0r.toortools.phantomq;


import com.object0r.toortools.os.OsHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhantomJsManager extends Thread
{
    static int workersCount = 5;
    static int jsTimeout = 30;

    private String phantomJsPath = null;

    public String getPhantomJsPath()
    {
        String thisPhantomJsPath = "";
        if (phantomJsPath == null)
        {
            if (OsHelper.isWindows())
            {
                thisPhantomJsPath = "bin/phantomjs.exe";
            }
            else
            {
                thisPhantomJsPath = "phantomjs";
            }
        }
        else
        {
            thisPhantomJsPath = this.phantomJsPath;
        }

        return thisPhantomJsPath;
    }

    boolean verifyPhantomJs()
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(this.getPhantomJsPath() + " --help");
            proc.waitFor(5, TimeUnit.SECONDS);
            int exitVal = proc.exitValue();
            if (exitVal != 0)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int getJsTimeOut()
    {
        return jsTimeout;
    }

    public PhantomJsManager(int workerCount, boolean useTor) throws Exception
    {
        this.workersCount = workerCount;
        this.useTor = useTor;
        if (!verifyPhantomJs())
        {
            throw new Exception("Phantom js path is not valid: " + getPhantomJsPath());
        }
    }

    public PhantomJsManager(int workerCount, String phantomJsPath, boolean useTor) throws Exception
    {
        this.workersCount = workerCount;
        this.phantomJsPath = phantomJsPath;
        this.useTor = useTor;
        if (!verifyPhantomJs())
        {
            throw new Exception("Phantom js path is not valid: " + getPhantomJsPath());
        }
    }

    HashMap<String, PhantomJsJob> jobs = new HashMap<String, PhantomJsJob>();
    boolean useTor = false;

    synchronized public PhantomJsJob addJob(PhantomJsJob job)
    {
        jobs.put(job.getUrl(), job);
        return job;
    }

    synchronized public PhantomJsJob addJob(String url)
    {
        if (jobs.containsKey(url))
        {
            return jobs.get(url);
        }
        else
        {
            PhantomJsJob phantomJsJob = new PhantomJsJob(url);
            jobs.put(url, phantomJsJob);
            return phantomJsJob;
        }
    }

    synchronized public PhantomJsJob addJob(String url, String body)
    {
        String key = url + body;
        if (jobs.containsKey(key))
        {
            return jobs.get(key);
        }
        else
        {
            PhantomJsJob phantomJsJob = new PhantomJsJob(key, body);
            jobs.put(key, phantomJsJob);
            return phantomJsJob;
        }
    }

    public void run()
    {
        for (int i = 0; i < workersCount; i++)
        {
            new PhantomJsWorker(this, useTor).start();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
        }

        while (true)
        {
            try
            {
                Thread.sleep(20000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Similar to pop.
     *
     * @return
     */
    synchronized PhantomJsJob getNextJob()
    {
        for (Map.Entry<String, PhantomJsJob> entry : jobs.entrySet())
        {
            String key = entry.getKey();
            PhantomJsJob phantomJsJob = entry.getValue();
            if (phantomJsJob.isPending())
            {
                phantomJsJob.setStatusProcessing();
                return phantomJsJob;
            }
        }
        return null;
    }
}
