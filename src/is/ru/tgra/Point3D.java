package is.ru.tgra;

public class Point3D
{
	public float x;
	public float y;
	public float z;
	
	public Point3D(float xx, float yy, float zz)
	{
		x = xx;
		y = yy;
		z = zz;
	}
	
	public void set(float xx, float yy, float zz)
	{
		x = xx;
		y = yy;
		z = zz;
	}
	
	public void add(Vector3D v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}

    public static float LengthLine(Point3D p0, Point3D p1, Point3D p2){
        return (Vector3D.cross(Vector3D.difference(p0,p1),Vector3D.difference(p0,p2)).length())/(Vector3D.difference(p2,p1).length());
    }
}
