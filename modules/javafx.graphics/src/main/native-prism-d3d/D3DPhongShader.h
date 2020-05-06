/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

#ifndef D3DPHONGSHADER_H
#define D3DPHONGSHADER_H

// VSR implies Vertex Shader Registers
#define VSR_VIEWPROJMATRIX  0  // 4 total
#define VSR_CAMERAPOS 4        // 1 total
// lighting
// number of lights used out of max lights
#define VSR_NUM_LIGHTS 5
// 5 lights with 2 registers (pos + color) = 10 registers
#define VSR_LIGHTS 10
// 8 ambient points + 2 coords : 10 registers
#define VSR_AMBIENTCOLOR 30
// world
#define VSR_WORLDMATRIX 40

// PSR implies Pixel Shader Registers
// we have 32 constants for ps 2.0
#define PSR_DIFFUSECOLOR 0
#define PSR_SPECULARCOLOR 1
#define PSR_NUM_LIGHTS 3
#define PSR_LIGHTCOLOR 4

// SR implies Sampler Registers
#define SR_DIFFUSEMAP 0
#define SR_SPECULARMAP 1
#define SR_BUMPHEIGHTMAP 2
#define SR_SELFILLUMMAP 3

enum SpecType {
    SpecNone,
    SpecTexture, // map only w/o alpha
    SpecColor,   // color w/o map
    SpecMix,     // map & color
    SpecTotal
};

enum BumpType {
    BumpNone,
    BumpSpecified,
    BumpTotal
};

// i = self illum
// n = SpecNone
// t = SpecTexture
// c = SpecColor
// m = SpecMix
// s = BumpNone
// b = BumpSpecified
typedef const DWORD * ShaderFunction;
ShaderFunction vsMtl1_Obj();
ShaderFunction psMtl1(), psMtl1_i(),
psMtl1_sn(),
psMtl1_st(),
psMtl1_sc(),
psMtl1_sm(),

psMtl1_bn(),
psMtl1_bt(),
psMtl1_bc(),
psMtl1_bm(),

psMtl1_sni(),
psMtl1_sti(),
psMtl1_sci(),
psMtl1_smi(),

psMtl1_bni(),
psMtl1_bti(),
psMtl1_bci(),
psMtl1_bmi();

class D3DPhongShader {
public:
    D3DPhongShader(IDirect3DDevice9 *dev);
    virtual ~D3DPhongShader();
    IDirect3DVertexShader9 *getVertexShader();
    int getBumpMode(bool isBumpMap);
    int getSpecularMode(bool isSpecularMap, bool isSpecularColor);
    HRESULT setPixelShader(int numLights, int specularMode, int bumpMode, int selfIllumMode);

static const int SelfIlllumTotal = 2;
static const int maxLights = 5;

private:
    IDirect3DDevice9 *device;
    IDirect3DVertexShader9 *vertexShader;
    IDirect3DPixelShader9 *pixelShader0, *pixelShader0_si;
    IDirect3DPixelShader9 *pixelShaders[SelfIlllumTotal][BumpTotal][SpecTotal]/*[maxLights]*/; // 2 * 2 * 4
};

#endif  /* D3DPHONGSHADER_H */
