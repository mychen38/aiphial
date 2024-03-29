/*
 *
 * This file is part of Aiphial.
 *
 * Copyright (c) 2010 Nicolay Mitropolsky <NicolayMitropolsky@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package MyImage.utls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import me.uits.aiphial.general.basic.Cluster;
import me.uits.aiphial.general.basic.Utls;

import me.uits.aiphial.imaging.LuvPoint;
import static java.lang.Math.*;

/**
 *
 * @author Nicolay Mitropolsky <NicolayMitropolsky@gmail.com>
 */
public class ClustersMap
{
    //TODO возможно его стоит сделать наследником PointsMap

    private Cluster<LuvPoint>[][] array;
    //TODO replace in with PointMap
    private PointsMap pointsMap;
    protected int sx;
    protected int sy;
    int width;
    int height;
    Map<Cluster<LuvPoint>, Collection<LuvPoint>> bounary8map;
    Map<Cluster<LuvPoint>, Collection<LuvPoint>> bounary4map;


    public ClustersMap(int x, int y, int width, int height, int add)
    {
        this.sx = x-add;
        this.sy = y-add;
        this.width = width+add;
        this.height = height+add;
        array = new Cluster[this.width-sx][this.height-sy];
        //points = new LuvPoint[width][height];
        pointsMap = new PointsMap(x, y, width, height, add);
    }

    public ClustersMap(int width, int height)
    {
        this(0, 0, width, height, 1);
    }


    public void buildMap(Iterable<Cluster<LuvPoint>> clusters)
    {
        int count = 0;


        for (Cluster<LuvPoint> cluster : clusters)
        {
            count++;
            for (LuvPoint point : cluster)
            {
                array[point.getX()-sx][point.getY()-sy] = cluster;
                //points[point.getX()][point.getY()] = point;
                pointsMap.putAt(point.getX(), point.getY(), point);
            }


        }
        buildBoundaryes(clusters, count);

    }

    private void buildBoundaryes(Iterable<Cluster<LuvPoint>> clusters, int count)
    {
        bounary8map = new HashMap<Cluster<LuvPoint>, Collection<LuvPoint>>(count);
        bounary4map = new HashMap<Cluster<LuvPoint>, Collection<LuvPoint>>(count);
        for (Cluster<LuvPoint> cluster : clusters)
        {

            bounary8map.put(cluster, buildcluster8boundary(cluster));
            bounary4map.put(cluster, buildcluster4boundary(cluster));
        }
    }

    public Cluster getAt(LuvPoint point)
    {
        return getAt(point.getX(), point.getY());
    }

    public Cluster getAt(int x, int y)
    {
        return array[x-sx][y-sy];
    }

    public LuvPoint getPointAt(int x, int y)
    {
        return pointsMap.getAt(x, y);
    }

    public Collection<LuvPoint> get8Boundary(Cluster cluster)
    {
        return Collections.unmodifiableCollection(bounary8map.get(cluster));
    }

    public Collection<LuvPoint> get4Boundary(Cluster cluster)
    {
        return Collections.unmodifiableCollection(bounary4map.get(cluster));
    }

    private Collection<LuvPoint> buildcluster8boundary(Cluster<LuvPoint> cluster)
    {
        Collection<LuvPoint> boundary = new ArrayList<LuvPoint>();
        for (LuvPoint point : cluster)
        {
            if (isOnBoundary(point, cluster))
            {
                boundary.add(point);
            }
        }
        return boundary;
    }

    private Collection<LuvPoint> buildcluster4boundary(Cluster<LuvPoint> cluster)
    {
        Collection<LuvPoint> boundary = new ArrayList<LuvPoint>();
        for (LuvPoint point : cluster)
        {
            if (isOn4Boundary(point, cluster))
            {
                boundary.add(point);
            }
        }
        return boundary;
    }

    public boolean isOnBoundary(LuvPoint point, Cluster cluster)
    {

        for (LuvPoint p : pointsMap.get8Nearest(point))
        {
            if (getAt(p.getX(), p.getY()) != cluster)
            {
                return true;
            }
        }

        return false;

    }

    public boolean isOn4Boundary(LuvPoint point, Cluster cluster)
    {
        for (LuvPoint p : pointsMap.get4Nearest(point))
        {
            if (getAt(p.getX(), p.getY()) != cluster)
            {
                return true;
            }
        }

        return false;
    }

    public Collection<LuvPoint> get8NearestWithinCluster(Cluster<LuvPoint> cluster, LuvPoint point)
    {
        Collection<LuvPoint> result = new ArrayList<LuvPoint>(8);

        for (LuvPoint l : get8Nearest(point))
        {
            if (getAt(l.getX(), l.getY()) == cluster)
            {
                result.add(l);
            }
        }

        return result;
    }

    public Iterable<LuvPoint> get8Nearest(LuvPoint point)
    {
        return pointsMap.get8Nearest(point);
    }

    public Iterable<LuvPoint> get4Nearest(LuvPoint point)
    {
        return pointsMap.get4Nearest(point);
    }

    public Collection<LuvPoint> getOrderedBoundary(Cluster cluster)
    {

        return new BugInnerBO(this).getOrderedBoundary(cluster);
        //return new BugOuterBO(this).getOrderedBoundary(cluster);

        //return new RegionGrowBO(this).getOrderedBoundary(cluster);
        //return new BoundaryOrderer(cluster, get8Boundary(cluster)).getOrdered();

    }

    public Collection<Collection<LuvPoint>> getOrderedBoundaries(Cluster cluster)
    {

        return new BugInnerBO(this).getOrderedBoundaryes(cluster);

    }

    public PointsMap getPointsMap()
    {
        return pointsMap;
    }
}
