/*
$ java NBody 157788000.0 25000.0 source_file_in_data
 */
public class NBody
{
    public static double readRadius(String file)
    {
        In in = new In(file);
        int num = in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String file)
    {
        In in = new In(file);
        int num = in.readInt();
        in.readDouble();    /* read radius */
        Planet[] planets = new Planet[num];
        for (int i = 0; i < num; i ++)
        {
            planets[i] =new Planet(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return planets;
    }

    public static void main(String[] args)
    {
        /* read from commandline */
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        int n = planets.length;

        StdDraw.setScale(-radius, radius);
        /* prevent animation flickering */
        StdDraw.enableDoubleBuffering();
        /* draw starfield */
        /* draw planets */
        for (Planet p : planets)
        {
           p.draw();
        }

        double time = 0;
        while (time < T)
        {
            time += dt;

            /* calc forces */
            double[] xForces = new double[n], yForces = new double[n];
            for (int i = 0; i < n; i ++)
            {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            /* update and draw */
            StdDraw.clear();
            StdDraw.picture(0, 0,"images/starfield.jpg");
            for (int i = 0; i < n; i ++)
            {
                planets[i].update(dt, xForces[i], yForces[i]);
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        /* print out the final state */
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
