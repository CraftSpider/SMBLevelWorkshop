package com.owens.oobjloader.lwjgl;

// This code was written by myself, Sean R. Owens, sean at guild dot net,
// and is released to the public domain. Share and enjoy. Since some
// people argue that it is impossible to release software to the public
// domain, you are also free to use this code under any version of the
// GPL, LPGL, Apache, or BSD licenses, or contact me for use of another
// license.  (I generally don't care so I'll almost certainly say yes.)
// In addition this code may also be used under the "unlicense" described
// at http://unlicense.org/ .  See the file UNLICENSE in the repo.

import craftedcart.smblevelworkshop.resource.ResourceShaderProgram;
import io.github.craftedcart.fluidui.util.UIColor;

import java.util.ArrayList;

public class DisplayModel {

    public ArrayList<VBO> vboList = new ArrayList<VBO>();

    public DisplayModel() {
    }

    public void addVBO(VBO r) {
        vboList.add(r);
    }

    public void render(ResourceShaderProgram shaderProgram, boolean setTexture) {
        for (int loopi = 0; loopi < vboList.size(); loopi++) {
            vboList.get(loopi).render(shaderProgram, setTexture);
        }
    }

    public void render(ResourceShaderProgram shaderProgram, boolean setTexture, UIColor color) {
        for (int loopi = 0; loopi < vboList.size(); loopi++) {
            vboList.get(loopi).render(shaderProgram, setTexture, color);
        }
    }

}