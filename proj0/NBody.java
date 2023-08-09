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
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        StdDraw.picture(0, 0,"images/starfield.jpg");
        for (Planet p : planets)
        {
           p.draw();
        }

        StdDraw.show();
        StdDraw.pause(1000);
    }
}
