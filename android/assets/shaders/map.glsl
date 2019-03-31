/*** VERTEX SHADER ***/
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
    v_color = vec4(1, 1, 1, 1);
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;
}


/*** FRAGMENT SHADER ***/
#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main()
{
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}
