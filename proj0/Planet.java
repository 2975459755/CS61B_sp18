public class Planet
{
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;

    final static double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img)
    {
        this.xxPos = xP; this.yyPos = yP;
        this.xxVel = xV; this.yyVel = yV;
        this.mass = m; this.imgFileName = img;
    }
    public Planet(Planet p)
    {
        this.xxPos = p.xxPos; this.yyPos = p.yyPos;
        this.xxVel = p.xxVel; this.yyVel = p.yyVel;
        this.mass = p.mass; this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p)
    {
        return Math.sqrt(Math.pow(this.xxPos - p.xxPos, 2) + Math.pow(this.yyPos - p.yyPos, 2));
    }
    public double calcForceExertedBy(Planet p)
    {
        return G * this.mass * p.mass / Math.pow(this.calcDistance(p), 2);
    }
    public double calcForceExertedByX(Planet p)
    {
        return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / calcDistance(p);
    }
    public double calcForceExertedByY(Planet p)
    {
        return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / calcDistance(p);
    }
    public double calcNetForceExertedByX(Planet[] arr)
    {
        double sum = 0;
        for (Planet p : arr)
        {
            if (p.equals(this))
            {
                continue;
            }
            else
            {
                sum += this.calcForceExertedByX(p);
            }
        }
        return sum;
    }
    public double calcNetForceExertedByY(Planet[] arr)
    {
        double sum = 0;
        for (Planet p : arr)
        {
            if (p.equals(this))
            {
                continue;
            }
            else
            {
                sum += this.calcForceExertedByY(p);
            }
        }
        return sum;
    }
    public void update(double dt, double xxForce, double yyForce)
    {
        double xxAcc = xxForce / mass, yyAcc = yyForce / mass;
        xxVel += xxAcc * dt; yyVel += yyAcc * dt;
        xxPos += xxVel * dt; yyPos += yyVel * dt;
    }
    public void draw()
    {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
