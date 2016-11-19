package com.object0r.toortools.phantomq;

public class Main
{
    public static void main(String args[])
    {
        try
        {
            PhantomJsManager manager = null;
            try
            {
                manager = new PhantomJsManager("D:\\Projects\\proxymity\\bin\\phantomjs.exe", false);
                manager.start();
                PhantomJsJob job = manager.addJob("http://www.bbc.co.uk/");
                while (!job.isFinished())
                {
                    System.out.println("Download is not ready. Sleeping.");
                    Thread.sleep(5000);
                }
                if (job.isSuccessful())
                {
                    System.out.println(job.getPhantomJsJobResult().getSourceCode());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
