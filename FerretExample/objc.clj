(native-declare "#import <CoreFoundation/CoreFoundation.h>")
(native-declare "#import <objc/runtime.h>")
(native-declare "#import <objc/message.h>")
(native-declare "#undef nil")

(native-declare "#define objc_msgSend(T) reinterpret_cast<T (*)(void *, SEL, ...)>(objc_msgSend)")
(native-declare "#define objc_msgSendSuper(T) reinterpret_cast<T (*)(void *, SEL, ...)>(objc_msgSendSuper)")

(defn objc-msg-send [self _cmd & args] "
  void *self_ = value<void *>::to_value(self);
  SEL _cmd_ = sel_registerName(string::c_str(string::pack(_cmd)));
  switch (rt::count(args)) {
      case 0:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_));
      case 1:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0))
          ));
      case 2:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1))
          ));
      case 3:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2))
          ));
      case 4:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2)),
              pointer::to_pointer<void *>(rt::nth(args, 3))
          ));
      case 5:
          return obj<pointer>(objc_msgSend(void *)(self_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2)),
              pointer::to_pointer<void *>(rt::nth(args, 3)),
              pointer::to_pointer<void *>(rt::nth(args, 4))
          ));
      default:
          abort();
  }
  ")

(defn objc-msg-send-super [sup _cmd & args] "
  objc_super *sup_ = pointer::to_pointer<objc_super>(sup);
  SEL _cmd_ = sel_registerName(string::c_str(string::pack(_cmd)));
  switch (rt::count(args)) {
      case 0:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_));
      case 1:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0))
          ));
      case 2:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1))
          ));
      case 3:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2))
          ));
      case 4:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2)),
              pointer::to_pointer<void *>(rt::nth(args, 3))
          ));
      case 5:
          return obj<pointer>(objc_msgSendSuper(void *)(sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2)),
              pointer::to_pointer<void *>(rt::nth(args, 3)),
              pointer::to_pointer<void *>(rt::nth(args, 4))
          ));
      default:
          abort();
  }
  ")

(defn cf-show [object] "
  void *object_ = value<void *>::to_value(object);
  CFShow(object_);
  ")

(defn objc-get-class [name] "
  const char *name_ = string::c_str(string::pack(name));
  Class cls_ = objc_getClass(name_);
  __result = obj<value<Class>>(cls_);
  ")

(defn objc-allocate-class-pair [superclass, name, extra] "
  Class superclass_ = value<Class>::to_value(superclass);
  const char *name_ = string::c_str(string::pack(name));
  size_t extra_ = number::to<size_t>(extra);
  Class cls_ = objc_allocateClassPair(superclass_, name_, extra_);
  __result = obj<value<Class>>(cls_);
  ")

(defn objc-register-class-pair [cls] "
  Class cls_ = value<Class>::to_value(cls);
  objc_registerClassPair(cls_);
  ")

(defn class-get-name [cls] "
  Class cls_ = value<Class>::to_value(cls);
  const char *name_ = class_getName(cls_);
  __result = obj<string>(name_);
  ")

(defn class-get-super-class [cls] "
  Class cls_ = value<Class>::to_value(cls);
  Class superclass_ = class_getSuperclass(cls_);
  __result = obj<value<Class>>(superclass_);
  ")

(defn class-add-method [cls name imp types] "
  Class cls_ = value<Class>::to_value(cls);
  const char *name_ = string::c_str(string::pack(name));
  SEL selector_ = sel_registerName(name_);
  IMP imp_ = value<IMP>::to_value(imp);
  const char *types_ = string::c_str(string::pack(types));
  BOOL success_ = class_addMethod(cls_, selector_, imp_, types_);
  __result = obj<boolean>(success_);
  ")

(defn object-get-instance-variable [object name] "
  id object_ = value<id>::to_value(object);
  const char *name_ = string::c_str(string::pack(name));
  void *v_ = nullptr;
  Ivar ivar_ = object_getInstanceVariable(object_, name_, &v_);
  var v = obj<pointer>(v_);
  var ivar = obj<value<Ivar>>(ivar_);
  __result = rt::cons(ivar, v);
  ")

(defn object-set-instance-variable [object name v] "
  id object_ = value<id>::to_value(object);
  const char *name_ = string::c_str(string::pack(name));
  void *v_ = value<void *>::to_value(v);
  Ivar ivar_ = object_setInstanceVariable(object_, name_, v_);
  __result = obj<value<Ivar>>(ivar_);
  ")

(defn fstr->cfstr [s] "
  const char *s_ = string::c_str(string::pack(s));
  CFStringRef cfstring_ = CFStringCreateWithCString(kCFAllocatorDefault, s_, kCFStringEncodingUTF8);
  __result = obj<value<CFStringRef>>(cfstring_);
  ")

(defn cfstr->fstr [s] "
  CFStringRef s_ = value<CFStringRef>::to_value(s);
  const char *cstring_ = CFStringGetCStringPtr(s_, kCFStringEncodingUTF8);
  __result = obj<string>(cstring_);
  ")

(defn fstr->cstr [s] "
  const char *s_ = string::c_str(string::pack(s));
  __result = obj<value<const char *>>(s_);
  ")

(defn cstr->fstr [s] "
  const char *s_ = value<const char *>::to_value(s);
  __result = obj<string>(s_);
  ")

(native-declare
  "
  @interface FRTObject : NSObject
  - (id)initWithFRTObject:(ferret::ref)object;
  - (ferret::var)object;
  @end

  @implementation FRTObject {
      ferret::var _object;
  }

  - (id)initWithFRTObject:(ferret::ref)object {
      self = [super init];
      if (self) {
          _object = object;
      }
      return self;
  }

  - (ferret::var)object {
      return _object;
  }

  @end
  ")

(defn object->var [o] "
  FRTObject *o_ = value<FRTObject *>::to_value(o);
  __result = [o_ object];
  ")

(defn imp-implementation-with-ferret-lambda [arity lambda] "
      size_t arity_ = number::to<size_t>(arity);
      lambda_i *lambda_ = lambda.cast<lambda_i>();
      lambda_->inc_ref();
      id (^block_)(id, void *...) = ^(id self_, void *args_...){
          va_list ap;
          va_start(ap, args_);
          var args = nil();
          for (size_t i_ = 0; i_ < arity_; i_++) {
              var arg = obj<value<void *>>(va_arg(ap, void *));
              args = rt::cons(arg, args);
          }
          va_end(ap);
          var self = obj<value<id>>(self_);
          var object = lambda_->invoke(args);
          return [[[FRTObject alloc] initWithFRTObject:object] autorelease];
      };
      IMP imp_ = imp_implementationWithBlock(block_);
      __result = obj<value<IMP>>(imp_);
  ")