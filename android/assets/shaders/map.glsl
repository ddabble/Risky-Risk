/*** VERTEX SHADER ***/
// These are provided by SpriteBatch
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

// Provided by SpriteBatch
uniform mat4 u_projTrans;

varying vec2 v_texCoords;

void main()
{
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;
}


/*** FRAGMENT SHADER ***/
#ifdef GL_ES
    precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
// TODO: use a texture to look up player colors
// 224 is the max number of allowed uniform vectors in a fragment shader in OpenGL ES 3.0
uniform vec3 playerColorLookup[224]; //0xFF + 1];

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float red_hex = texColor.r * float(0xFF);
    int territoryIndex = int(round(red_hex));
    gl_FragColor = vec4(playerColorLookup[territoryIndex], texColor.a);
}
