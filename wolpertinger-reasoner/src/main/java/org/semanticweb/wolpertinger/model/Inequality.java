/*  Copyright 2015 by the International Center for Computational Logic, Technical University Dresden.
 
    This file is part of Wolpertinger.

    Wolpertinger is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Wolpertinger is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Wolpertinger.  If not, see <http://www.gnu.org/licenses/>.
*/
/* Copyright 2008, 2009, 2010 by the Oxford University Computing Laboratory
   
   This file is part of HermiT.

   HermiT is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   HermiT is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public License
   along with HermiT.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.wolpertinger.model;

import java.io.Serializable;

import org.semanticweb.wolpertinger.Prefixes;

/**
 * Represents the inequality predicate.
 */
public class Inequality implements DLPredicate,Serializable {
    private static final long serialVersionUID=296924110684230279L;

    public static final Inequality INSTANCE=new Inequality();
    
    protected Inequality () {
    }
    public int getArity() {
        return 2;
    }
    public String toString(Prefixes prefixes) {
        return "!=";
    }
    public String toString() {
        return toString(Prefixes.STANDARD_PREFIXES);
    }
    protected Object readResolve() {
        return INSTANCE;
    }
    public static Inequality create() {
        return INSTANCE;
    }
}
