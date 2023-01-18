/*
* This file was generated by PolyTaint code partitioner - ERO project 2022
*
* The MIT License (MIT)
* Copyright (c) 2022 anonymous-xh anonymous-xh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software
* and associated documentation files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
* and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
* subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial
* portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


#include "Proxy_Out.h"
#include "graal_isolate.h"
#include "Enclave_u.h"
#include "main.h"

extern sgx_enclave_id_t global_eid;
extern graal_isolatethread_t *global_app_iso;

void ocall_sayHello() {
    sayHello_entry(global_app_iso);
}
int ocall_arraySum(void* param1,int len1) {
    return arraySum_entry(global_app_iso, (char*)param1,len1);
}
void* ocall_readXData(int param1) {
    return readXData_entry(global_app_iso, param1);
}
double funcA_proxy() {
    double ret;
    ecall_funcA(global_eid,&ret);
    return ret;
}
