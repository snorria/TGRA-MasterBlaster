package is.ru.tgra;

public class Vector3D
{
	public float x;
	public float y;
	public float z;
	
	public Vector3D(float xx, float yy, float zz)
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
	
	public float length()
	{
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	
	public void normalize()
	{
		float len = length();
		x = x / len;
		y = y / len;
		z = z / len;
	}
	
	public static Vector3D difference(Point3D P1, Point3D P2)
	{
		return new Vector3D(P1.x - P2.x, P1.y - P2.y, P1.z - P2.z);
	}
	
	public static float dot(Vector3D v1, Vector3D v2)
	{
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	public static Vector3D cross(Vector3D P1, Vector3D P2)
	{
		return new Vector3D(P1.y*P2.z - P1.z*P2.y, P1.z*P2.x - P1.x*P2.z, P1.x*P2.y - P1.y*P2.x);
	}
	
	public static Vector3D mult(float s, Vector3D v)
	{
		return new Vector3D(s*v.x, s*v.y, s*v.z);
	}

	public static Vector3D sum(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
	}

    public static double angle(Vector3D v1, Vector3D v2)
    {
        /*float dot = dot(v1,v2);
        float lenSq1 = v1.x*v1.x + v1.y*v1.y + v1.z*v1.z;
        float lenSq2 = v2.x*v2.x + v2.y*v2.y + v2.z*v2.z;

        return Math.acos(dot/Math.sqrt(lenSq1 * lenSq2))*180/Math.PI;*/


        return Math.acos((Vector3D.dot(v1,v2)/(v1.length()*v2.length())))*180/Math.PI;
    }
}
