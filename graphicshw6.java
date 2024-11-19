

//		for all spheres in the view plane
	//		for all x,y in the view plane
		//		calculate intersection
			//	if intersection
				//	color pixel that has the smallest t 
					//keep track of t value in 2d array
					
					//if t[x][y] > intersection[x][y]
						//t[x][y] = intersection[x][y] //gets the value of the intersection point of the second sphere
						
						//if 2 t values are exactly the same just pick a color.
						//modify whatever reads the file to read light sources
	//}
//}

//CHANGE THE COLORING PART FOR THIS ASSIGNMENT










import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;


public class graphicshw6 
{
    // Constants
    static int WIDTH = 1024;
    static int HEIGHT = 768;
    static int NUMSPHERES = 5; // Adjust as needed

    // Image buffer and color arrays
    static int[][] R = new int[WIDTH][HEIGHT];
    static int[][] G = new int[WIDTH][HEIGHT];
    static int[][] B = new int[WIDTH][HEIGHT];
	
	static double[][] tarray = new double[WIDTH][HEIGHT];
    static point llist[]; 
    static sphere slist[]; 
	
    static BufferedImage buffer;
    static MyCanvas canvas;

    public static void initimage() {
        int j, k;

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (j = 0; j < WIDTH; j++) {
            for (k = 0; k < HEIGHT; k++) {
                R[j][k] = G[j][k] = B[j][k] = 255;
            }
        }
    }

    public static void main(String[] args) 
	{
		/*
		 * Necessary AWT/Swing steps.
		 */
		JFrame	frame = new JFrame();
		canvas = new MyCanvas();
		frame.add (canvas, "Center");

		/*
		 * Boilerplate. Just do this. I don't even 
		 * remember what it all does any more, but 
		 * it's required.
		 */
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Change if you like.
		frame.setTitle("CSCI 365");

		/*
		 * Y > HEIGHT because of "Window Decorations" 
		 * (Java's terminology, not mine.)
		 */ 
		frame.setSize(WIDTH, HEIGHT+15);
		frame.setVisible(true);
		
		
		
        initimage();
		displayimage();

        // variables
        Scanner filereader = new Scanner(System.in);
        int x=0, y=0, z=0;
        double t = 0;
        double dx, dy, dz = 0;
        double A, D, C = 0;
        int Sr = 0; // sphere radius
        double discriminant = 0;
        int spherenumb = 0;
        int viewz;
		int sx, sy, sz = 0;
		int lx, ly, lz = 0; //vector of the center of the sphere to the view plane
		int nx, ny, nz = 0;
		double magl, magn = 0; //magnitude
		double theta;
        double nl; //theta is the percent of the color you want to use
		double ka =.3;
        double kd =.7; //ambient light and diffuse light;  
		
		

        try 
		{
            System.out.printf("File Name: ");

            String filename = filereader.nextLine();

            File instructions = new File(filename);
            Scanner myReader = new Scanner(instructions);

            point eye = new point();
            myReader.next();
            eye.x = myReader.nextInt();
            eye.y = myReader.nextInt();
            eye.z = myReader.nextInt();

            myReader.next();
            viewz = myReader.nextInt();
            myReader.next();
            spherenumb = myReader.nextInt();

            slist = new sphere[spherenumb]; //This was declared wrong
 
            for (int i = 0; i < spherenumb; i++) 
			{
                slist[i] = new sphere();
                myReader.next();
                slist[i].center.x = myReader.nextInt();   //So in the Sphere class, x,y,z are all accesed through "center"
                slist[i].center.y = myReader.nextInt();
                slist[i].center.z = myReader.nextInt();
                slist[i].radius = myReader.nextInt();
                myReader.next();
                slist[i].R = myReader.nextInt();
                slist[i].G = myReader.nextInt();
                slist[i].B = myReader.nextInt();
            }
			//myReader.next();
			
            int lightnum = 0;
            myReader.next();
            lightnum = myReader.nextInt();
            System.out.printf("\t%d Light/Lights\n", lightnum);
			
            llist = new point[lightnum];

			//basically copy other for loop
			for (int i = 0; i < lightnum; i++) 
			{
                llist[i] = new point();
                myReader.next();
                llist[i].x = myReader.nextInt();
                llist[i].y = myReader.nextInt();
                llist[i].z = myReader.nextInt();
            }
			
        
			
            myReader.close();

            for (int i = 0; i < spherenumb; i++) 
			{
                for (int j = 0; j < WIDTH; j++) 
				{
					z = viewz;
					
                    for (int k = 0; k < HEIGHT; k++) 
					{
						//initialize every value of the array to have the max value (MAX_DOUBLE) 
						//if t<tarray[x][y]
							//make new t that smaller number
						
                        dx = j - eye.x;
                        dy = k - eye.y;
                        dz = viewz - eye.z;

                        A = dx * dx + dy * dy + dz * dz;
                        D = 2 * (dx * (eye.x - slist[i].center.x) + dy * (eye.y - slist[i].center.y) + dz * (eye.z - slist[i].center.z));
                        C = (eye.x * eye.x) + (eye.y * eye.y) + (eye.z * eye.z) + (slist[i].center.x * slist[i].center.x) + (slist[i].center.y * slist[i].center.y) + (slist[i].center.z * slist[i].center.z) - (2 * eye.x * slist[i].center.x) - (2 * eye.y * slist[i].center.y) - (2 * eye.z * slist[i].center.z) - (slist[i].radius * slist[i].radius);

                        discriminant = D * D - 4 * A * C;
                        t = (-D - Math.sqrt(discriminant)) / (2 * A);

                       
                        if (discriminant >= 0 && t <= 0) 
						{ 
						    for (int p = 0; p < lightnum; p++) 
			                {       
								//math
								lx = (slist[i].center.x - j);
								ly = (slist[i].center.y - k);
								lz = (slist[i].center.z - viewz);

								nx = slist[i].center.x - llist[p].x;
								ny = slist[i].center.y - llist[p].y;
								nz = slist[i].center.z -llist[p].z;

								magl = Math.sqrt((lx*lx) + (ly*ly) + (lz*lz));
								magn = Math.sqrt((nx*nx) + (ny*ny) + (nz*nz));
								nl = nx*lx + ny*ly + ny*ly + nz*lz;
						
								theta = nl/(magl*magn);
								
								// Color the pixel
								R[j][k] = (int)((ka * (slist[i].R)) + (kd * theta * (slist[i].R)));
								G[j][k] = (int)((ka * (slist[i].G)) + (kd * theta * (slist[i].G)));
								B[j][k] = (int)((ka * (slist[i].B)) + (kd * theta * (slist[i].B)));
                            }
                        }
                    }
                }
            }
        } 
		catch (FileNotFoundException e) 
		{
            System.out.printf("FILE NOT FOUND");
            e.printStackTrace();
        }
		displayimage();
    }

    public static void printmenu() {
        System.out.printf("Menu:\n");
        System.out.printf("d:\tdisplay image in memory\n");
        System.out.printf("x:\texit\n");
        System.out.printf("Enter an option: ");
    }

    public static void readimage(String name) {
        int j, k;
        Color c;
        try {
            buffer = ImageIO.read(new File(name));
            for (j = 0; j < WIDTH; j++) {
                for (k = 0; k < HEIGHT; k++) {
                    c = new Color(buffer.getRGB(j, k));
                    R[j][k] = c.getRed();
                    G[j][k] = c.getGreen();
                    B[j][k] = c.getBlue();
                }
            }
        } catch (IOException e) {
            System.out.println("HALP ME.");
            e.printStackTrace();
        }
    }

    public static void displayimage() {
        int j, k;
        for (j = 0; j < WIDTH; j++) {
            for (k = 0; k < HEIGHT; k++) {
                buffer.setRGB(j, k, ((R[j][k] << 16 | G[j][k] << 8 | B[j][k])));
            }
        }
        canvas.repaint();
    }

    public static class MyCanvas extends JPanel {
        public MyCanvas() {
            super(true);
        }

        public void paint(Graphics g) {
            g.drawImage(buffer, 0, 0, Color.red, null);
        }
    }
}

//t stuff
