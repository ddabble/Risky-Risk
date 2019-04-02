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
uniform vec3 playerColorLookup[0xFF + 1];

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    int territoryIndex = int(texColor.r * 0xFF);
    gl_FragColor = vec4(playerColorLookup[territoryIndex], texColor.a);
}
