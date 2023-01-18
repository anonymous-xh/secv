/* Copyright (C) 2002-2014 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */

#ifndef _BITS_PTHREADTYPES_H
#define _BITS_PTHREADTYPES_H 1

#ifndef _BITS_PTHREADTYPES_COMMON_H
#define _BITS_PTHREADTYPES_COMMON_H 1

#include "wordsize.h"
#include <stdlib.h>

#ifdef __x86_64__
#if __WORDSIZE == 64
#define __SIZEOF_PTHREAD_ATTR_T 56
#define __SIZEOF_PTHREAD_MUTEX_T 40
#define __SIZEOF_PTHREAD_MUTEXATTR_T 4
#define __SIZEOF_PTHREAD_COND_T 48
#define __SIZEOF_PTHREAD_CONDATTR_T 4
#define __SIZEOF_PTHREAD_RWLOCK_T 56
#define __SIZEOF_PTHREAD_RWLOCKATTR_T 8
#define __SIZEOF_PTHREAD_BARRIER_T 32
#define __SIZEOF_PTHREAD_BARRIERATTR_T 4
#else
#define __SIZEOF_PTHREAD_ATTR_T 32
#define __SIZEOF_PTHREAD_MUTEX_T 32
#define __SIZEOF_PTHREAD_MUTEXATTR_T 4
#define __SIZEOF_PTHREAD_COND_T 48
#define __SIZEOF_PTHREAD_CONDATTR_T 4
#define __SIZEOF_PTHREAD_RWLOCK_T 44
#define __SIZEOF_PTHREAD_RWLOCKATTR_T 8
#define __SIZEOF_PTHREAD_BARRIER_T 20
#define __SIZEOF_PTHREAD_BARRIERATTR_T 4
#endif
#else
#define __SIZEOF_PTHREAD_ATTR_T 36
#define __SIZEOF_PTHREAD_MUTEX_T 24
#define __SIZEOF_PTHREAD_MUTEXATTR_T 4
#define __SIZEOF_PTHREAD_COND_T 48
#define __SIZEOF_PTHREAD_CONDATTR_T 4
#define __SIZEOF_PTHREAD_RWLOCK_T 32
#define __SIZEOF_PTHREAD_RWLOCKATTR_T 8
#define __SIZEOF_PTHREAD_BARRIER_T 20
#define __SIZEOF_PTHREAD_BARRIERATTR_T 4
#endif

/* Thread identifiers.  The structure of the attribute type is not
   exposed on purpose.  */
typedef unsigned long int pthread_t;

/**
 * Panonymous-xh: The following structures are normally not exposed.
 * Exposed here because they are needed in the enclave RT.
 */

/* Data structure to describe a process' schedulability.  */

struct graalsgx_sched_param
{
  int sched_priority;
};
/* Data structure to describe CPU mask.  */
typedef unsigned long __cpu_mask;
typedef struct
{
  __cpu_mask __bits[16];
} graalsgx_cpu_set_t;

struct pthread_attr
{
  /* Scheduler parameters and priority.  */
  struct graalsgx_sched_param schedparam;
  int schedpolicy;
  /* Various flags like detachstate, scope, etc.  */
  int flags;
  /* Size of guard area.  */
  size_t guardsize;
  /* Stack handling.  */
  void *stackaddr;
  size_t stacksize;
  /* Affinity map.  */
  graalsgx_cpu_set_t *cpuset;
  size_t cpusetsize;
};

union pthread_attr_t {
  char __size[__SIZEOF_PTHREAD_ATTR_T];
  long int __align;
};
#ifndef __have_pthread_attr_t
typedef union pthread_attr_t pthread_attr_t;
#define __have_pthread_attr_t 1
#endif

#ifdef __x86_64__
typedef struct __pthread_internal_list
{
  struct __pthread_internal_list *__prev;
  struct __pthread_internal_list *__next;
} __pthread_list_t;
#else
typedef struct __pthread_internal_slist
{
  struct __pthread_internal_slist *__next;
} __pthread_slist_t;
#endif

/* Data structures for mutex handling.  The structure of the attribute
   type is not exposed on purpose.  */
typedef union {
  struct __pthread_mutex_s
  {
    int __lock;
    unsigned int __count;
    int __owner;
#ifdef __x86_64__
    unsigned int __nusers;
#endif
    /* KIND must stay at this position in the structure to maintain
       binary compatibility.  */
    int __kind;
#ifdef __x86_64__
    short __spins;
    short __elision;
    __pthread_list_t __list;
#define __PTHREAD_MUTEX_HAVE_PREV 1
#define __PTHREAD_MUTEX_HAVE_ELISION 1
#else
    unsigned int __nusers;
    __extension__ union {
      struct
      {
        short __espins;
        short __elision;
#define __spins d.__espins
#define __elision d.__elision
#define __PTHREAD_MUTEX_HAVE_ELISION 2
      } d;
      __pthread_slist_t __list;
    };
#endif
  } __data;
  char __size[__SIZEOF_PTHREAD_MUTEX_T];
  long int __align;
} pthread_mutex_t;

typedef union {
  char __size[__SIZEOF_PTHREAD_MUTEXATTR_T];
  int __align;
} pthread_mutexattr_t;

/* Data structure for conditional variable handling.  The structure of
   the attribute type is not exposed on purpose.  */

/* Panonymous-xh: cond attr needed to be exposed */
/* Conditional variable attribute data structure.  */
struct pthread_condattr
{
  /* Combination of values:

     Bit 0  : flag whether conditional variable will be sharable between
	      processes.

     Bit 1-7: clock ID.  */
  int value;
};

typedef union {
  struct
  {
    int __lock;
    unsigned int __futex;
    __extension__ unsigned long long int __total_seq;
    __extension__ unsigned long long int __wakeup_seq;
    __extension__ unsigned long long int __woken_seq;
    void *__mutex;
    unsigned int __nwaiters;
    unsigned int __broadcast_seq;
  } __data;
  char __size[__SIZEOF_PTHREAD_COND_T];
  __extension__ long long int __align;
} pthread_cond_t;

typedef union {
  char __size[__SIZEOF_PTHREAD_CONDATTR_T];
  int __align;
} pthread_condattr_t;

/* Keys for thread-specific data */
typedef unsigned int pthread_key_t;

/* Once-only execution */
typedef int pthread_once_t;

#if defined __USE_UNIX98 || defined __USE_XOPEN2K
/* Data structure for read-write lock variable handling.  The
   structure of the attribute type is not exposed on purpose.  */
typedef union {
#ifdef __x86_64__
  struct
  {
    int __lock;
    unsigned int __nr_readers;
    unsigned int __readers_wakeup;
    unsigned int __writer_wakeup;
    unsigned int __nr_readers_queued;
    unsigned int __nr_writers_queued;
    int __writer;
    int __shared;
    unsigned long int __pad1;
    unsigned long int __pad2;
    /* FLAGS must stay at this position in the structure to maintain
       binary compatibility.  */
    unsigned int __flags;
#define __PTHREAD_RWLOCK_INT_FLAGS_SHARED 1
  } __data;
#else
  struct
  {
    int __lock;
    unsigned int __nr_readers;
    unsigned int __readers_wakeup;
    unsigned int __writer_wakeup;
    unsigned int __nr_readers_queued;
    unsigned int __nr_writers_queued;
    /* FLAGS must stay at this position in the structure to maintain
       binary compatibility.  */
    unsigned char __flags;
    unsigned char __shared;
    unsigned char __pad1;
    unsigned char __pad2;
    int __writer;
  } __data;
#endif
  char __size[__SIZEOF_PTHREAD_RWLOCK_T];
  long int __align;
} pthread_rwlock_t;

typedef union {
  char __size[__SIZEOF_PTHREAD_RWLOCKATTR_T];
  long int __align;
} pthread_rwlockattr_t;
#endif

#ifdef __USE_XOPEN2K
/* POSIX spinlock data type.  */
typedef volatile int pthread_spinlock_t;

/* POSIX barriers data type.  The structure of the type is
   deliberately not exposed.  */
typedef union {
  char __size[__SIZEOF_PTHREAD_BARRIER_T];
  long int __align;
} pthread_barrier_t;

typedef union {
  char __size[__SIZEOF_PTHREAD_BARRIERATTR_T];
  int __align;
} pthread_barrierattr_t;
#endif

#ifndef __x86_64__
/* Extra attributes for the cleanup functions.  */
#define __cleanup_fct_attribute __attribute__((__regparm__(1)))
#endif

#endif
#endif /* bits/pthreadtypes.h */
