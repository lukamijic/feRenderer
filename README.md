# feRenderer

feRenderer is 3D software renderer written in [Kotlin](https://kotlinlang.org/). That means it means that generating
image from a model is done by computer software and without relying on graphics hardware.

Rendering images from models is computing intensive problem and relies on GPU to make it run smoothly. However, the
point of this project was learning. I wanted to simulate the usual pipeline model goes through so image could be
generated. That means smooth running was a bit of a trade off.

## Contents

* [Math](#Math)
* [Starfield](#Starfield)
* [Drawing Lines](#drawing-lines)
* [Drawing triangles](#drawing-triangles)
* [Model Transformations](#model-transformations)
* [Camera](#Camera)
* [Object Loading](#object-loading)
* [Culling](#Culling)
* [Z - Buffer](#Z-Buffer)
* [Texture](#Texture)
* [Scenes Model](#Scenes-Model)
* [Lighting](#Lighting)
* [Solar System](#Solar-System)

### Math

3D computer graphics rely heavily on algebra. For manipulating models (scaling, translation, rotation etc.) I needed to
write small math library.

I used Kotlin features like [operator overloading](https://kotlinlang.org/spec/operator-overloading.html) and
[infix functions](https://kotlinlang.org/spec/operator-overloading.html) heavily. Those features enabled me to write
math formulas closer to how they would be written on paper.

```
val m1 = matrix(
            row(1, 3, 4.23),
            row(1.3, 3.3, 4.23)
        )
val m2 = matrix(
            row(5, 3.2, 2.43),
            row(3, 1.2, 2.60)
        )
val m3 = matrix(
            row(4, 1.2, 2.43),
            row(1, 0.34, 1.60)
        )
    
/** With overriding operators this */
m1.add(m2).times(2.0).minus(m3) 
/** becomes this */
(m1 + m2) * 2.0 - m3

val v1 = vector(2.23, 0.0, -3.213)
val v2 = vector(-0.233, 32.423, 0)

/** Using infix functions this */
v1.crossProduct(v2)
/** can be written as */
v1 x v2
``` 

A single bug in math library can cause multiple hard to debug issues in rendering. Because of that all math functions
were heavily tested.

### Starfield

Starfield is a standard demo for trying to draw something on display. For displaying the window and drawing the image
Java Swing is used. Drawing is done on single JFrame whose canvas is being provided with image array.

Swing Canvas provides some drawing methods for pixel, line, triangle and other shapes. Those functions were not used.
feRenderer uses custom Canvas method that provides only method for drawing pixel in an internal array. Whenever image is
ready to be shown that array is copied into an internal array of Swing Canvas.

<a href="https://youtu.be/TeqMjGbNjgA">
<img src="http://img.youtube.com/vi/TeqMjGbNjgA/0.jpg" width="32%"/>
</a>

### Drawing Lines

For drawing lines [Bresenham line algorithm](https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm) is used and for
line clipping [Cohen-Sutherland algorithm](https://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm) is used.

<p>
<img src="/media/static_lines.PNG" width="32%"/>
</p>
<p>
<img src="/media/line_clipping.PNG" width="32%"/>
</p>

### Drawing triangles

Triangles are most used polygons in computer graphics. One of the reasons is because you can always define a plane using
three dots in space. So triangles are building blocks for more complex models that are being rendered.

<p>
<img src="/media/filled_triangle_1.PNG" width="32%"/>
</p>
<p>
<img src="/media/filled_triangle_2.PNG" width="32%"/>
</p>

### Model Transformations

Models aren't supposed to just be static that is why transformation matrices for translation, scale, rotation were
implemented.

<a href="https://youtu.be/JYJ53YZ31G4">
<img src="http://img.youtube.com/vi/JYJ53YZ31G4/0.jpg" width="32%"/>
</a>

### Camera

After implementation of Projection matrix and screen transform matrix Camera matrix was implemented next. This allowed
camera to not be static (actually camera is still static, it still sits at (0, 0, 0) and looks down -z axis, but
everything else moves in other direction).

<a href="https://youtu.be/zG0ywZuBP2w">
<img src="http://img.youtube.com/vi/zG0ywZuBP2w/0.jpg" width="32%"/>
</a>

### Object Loading

Yes triangles are fun, but what about thousands of triangles?

<a href="https://youtu.be/E6lpbZqb2yw">
<img src="http://img.youtube.com/vi/E6lpbZqb2yw/0.jpg" width="32%"/>
</a>

### Culling

Some triangles are behind other triangles and drawing them is waste of resources. That is
why [culling](https://en.wikipedia.org/wiki/Back-face_culling) was implemented.

<a href="https://youtu.be/HMq822HNP-E">
<img src="http://img.youtube.com/vi/HMq822HNP-E/0.jpg" width="32%"/>
</a>

### Z Buffer

Sometimes parts of the object might be behind other objects, and they should be hidden. To handle that system
implements [Z - Buffer](https://en.wikipedia.org/wiki/Z-buffering)

<a href="https://youtu.be/qgv71SMOutU">
<img src="http://img.youtube.com/vi/qgv71SMOutU/0.jpg" width="32%"/>
</a>

### Texture

Objects being single colors is boring so adding of textures was implemented.

<p>
<img src="/media/textures.PNG" width="32%"/>
</p>

### Scenes Model

Objects position sometimes doesn't depend just on themselves. For example the Earth rotates around the Sun, but it also
moves together with the Sun across our galaxy and together with the galaxy across the universe. When the Sun moves we
want to move Earth as well, and when we move Earth we want it to move within Sun's system.

That is why scenes model was developed. It's a tree structure nodes can be scenes or objects, but objects are always
leaves.

<p>
<img src="/media/scenes.png" width="32%"/>
</p>

To define scene model within the code [DSL](https://en.wikipedia.org/wiki/Domain-specific_language) was created using
Kotlin. This helps us write more readable scene structures from which hierarchy can be easily understood.

```
# Without DSL
    val cubeMesh = ObjLoader.load("src/main/resources/obj/cube.obj")

    val earth = BitmapRenderObject(
        "earth",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/earth.jpg"),
        identityMatrix()
    )

    val moon = BitmapRenderObject(
        "moon",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/moon.jpg"),
        identityMatrix()
    )

    val sun = BitmapRenderObject(
        "sun",
        sphereMesh,
        BitmapLoader.load("src/main/resources/texture/sun.jpg"),
        identityMatrix()
    )

    val rootScene = RootScene("rootScene").apply {
        addChild(
            Scene("sunScene", translateMatrix(0.0, 20.0, -10.0)).apply {
                addRenderObject(sun)
                addChild(
                    Scene("earthScene", identityMatrix()).apply {
                        addRenderObject(earth)
                        addChild(Scene("moonScene", identityMatrix()).apply {
                            addRenderObject(moon)
                        })
                    }
                )
            }
        )
    }
```

```
# With DSL
rootScene {
    id = "rootScene"
    scenes {
        scene {
            id = "sunScene"
            modelViewMatrix = translateMatrix(0.0, 20.0, -10.0)
            renderObjects {
                bitmapRenderObject {
                    id = "sun"
                    mesh = sphereMesh
                    bitmapRes = "src/main/resources/texture/sun.jpg"
                }
            }
            scenes {
                scene {
                    id = "earthScene"
                    renderObjects {
                        bitmapRenderObject {
                            id = "earth"
                            mesh = sphereMesh
                            bitmapRes = "src/main/resources/texture/earth.jpg"
                        }
                    }
                    scenes {
                        scene {
                            id = "moonScene"
                            renderObjects {
                                bitmapRenderObject {
                                    id = "moon"
                                    mesh = sphereMesh
                                    bitmapRes = "src/main/resources/texture/moon.jpg"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

```

To reduce indentation I suggest separating scenes into different functions.

### Lighting
Light makes object feel more real. This system implements Phong's light system.

<a href="https://youtu.be/8ay_TBJdpLY">
<img src="http://img.youtube.com/vi/8ay_TBJdpLY/0.jpg" width="32%"/>
</a>

### Solar System
Everything mentioned thus far enabled creation of partial solar system.
This is final demo:

<a href="https://youtu.be/IpRmeXwteow">
<img src="http://img.youtube.com/vi/IpRmeXwteow/0.jpg" width="32%"/>
</a>
