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

(defn objc-msg-send-super [self _cmd & args] "
  id self_ = value<id>::to_value(self);
  struct objc_super sup_{self_, class_getSuperclass(object_getClass(self_))};
  SEL _cmd_ = sel_registerName(string::c_str(string::pack(_cmd)));
  switch (rt::count(args)) {
      case 0:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_));
      case 1:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0))
          ));
      case 2:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1))
          ));
      case 3:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2))
          ));
      case 4:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_,
              pointer::to_pointer<void *>(rt::nth(args, 0)),
              pointer::to_pointer<void *>(rt::nth(args, 1)),
              pointer::to_pointer<void *>(rt::nth(args, 2)),
              pointer::to_pointer<void *>(rt::nth(args, 3))
          ));
      case 5:
          return obj<pointer>(objc_msgSendSuper(void *)(&sup_, _cmd_,
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
  return obj<value<Class>>(cls_);
  ")

(defn objc-allocate-class-pair [superclass, name, extra] "
  Class superclass_ = value<Class>::to_value(superclass);
  const char *name_ = string::c_str(string::pack(name));
  size_t extra_ = 0;
  if (!extra.is_nil()) {
      extra_ = number::to<size_t>(extra);
  }
  Class cls_ = objc_allocateClassPair(superclass_, name_, extra_);
  return obj<value<Class>>(cls_);
  ")

(defn objc-register-class-pair [cls] "
  Class cls_ = value<Class>::to_value(cls);
  objc_registerClassPair(cls_);
  ")

(defn class-get-name [cls] "
  Class cls_ = value<Class>::to_value(cls);
  const char *name_ = class_getName(cls_);
  return obj<string>(name_);
  ")

(defn class-get-super-class [cls] "
  Class cls_ = value<Class>::to_value(cls);
  Class superclass_ = class_getSuperclass(cls_);
  return obj<value<Class>>(superclass_);
  ")

(defn class-add-method [cls name imp types] "
  Class cls_ = value<Class>::to_value(cls);
  const char *name_ = string::c_str(string::pack(name));
  SEL selector_ = sel_registerName(name_);
  IMP imp_ = value<IMP>::to_value(imp);
  const char *types_ = string::c_str(string::pack(types));
  BOOL success_ = class_addMethod(cls_, selector_, imp_, types_);
  return obj<boolean>(success_);
  ")

(defn object-get-instance-variable [object name] "
  id object_ = value<id>::to_value(object);
  const char *name_ = string::c_str(string::pack(name));
  void *v_ = nullptr;
  Ivar ivar_ = object_getInstanceVariable(object_, name_, &v_);
  var v = obj<pointer>(v_);
  var ivar = obj<value<Ivar>>(ivar_);
  return rt::cons(ivar, v);
  ")

(defn object-set-instance-variable [object name v] "
  id object_ = value<id>::to_value(object);
  const char *name_ = string::c_str(string::pack(name));
  void *v_ = value<void *>::to_value(v);
  Ivar ivar_ = object_setInstanceVariable(object_, name_, v_);
  return obj<value<Ivar>>(ivar_);
  ")

(defn fstr->cfstr [s] "
  const char *s_ = string::c_str(string::pack(s));
  CFStringRef cfstring_ = CFStringCreateWithCString(kCFAllocatorDefault, s_, kCFStringEncodingUTF8);
  return obj<value<CFStringRef>>(cfstring_);
  ")

(defn cfstr->fstr [s] "
  CFStringRef s_ = value<CFStringRef>::to_value(s);
  const char *cstring_ = CFStringGetCStringPtr(s_, kCFStringEncodingUTF8);
  return obj<string>(cstring_);
  ")

(defn fstr->cstr [s] "
  const char *s_ = string::c_str(string::pack(s));
  return obj<value<const char *>>(s_);
  ")

(defn cstr->fstr [s] "
  const char *s_ = value<const char *>::to_value(s);
  return obj<string>(s_);
  ")

(defn fnumber->native-int [x] "
  int x_ = number::to<int>(x);
  return obj<value<int>>(x_);
  ")

(defn fnumber->cfnumber [x] "
  double x_ = number::to<double>(x);
  CFNumberRef cfnumber_ = CFNumberCreate(kCFAllocatorDefault, kCFNumberDoubleType, &x_);
  return obj<value<CFNumberRef>>(cfnumber);
  ")

(defn cfnumber->fnumber [x] "
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
  return [o_ object];
  ")

(defn imp-implementation-with-ferret-lambda [lambda arity] "
      lambda_i *lambda_ = lambda.cast<lambda_i>();
      lambda_->inc_ref();
      size_t arity_ = number::to<size_t>(arity);
      id (^block_)(id, void *...) = ^(id self_, void *args_...){
          var args = nil();
          var self = obj<value<void *>>(self_);
          args = rt::cons(self, args);
          va_list ap;
          va_start(ap, args_);
          for (size_t i_ = 1; i_ < arity_; i_++) {
              var arg = obj<value<void *>>(va_arg(ap, void *));
              args = rt::cons(arg, args);
          }
          va_end(ap);
          var object = lambda_->invoke(args);
          return [[[FRTObject alloc] initWithFRTObject:object] autorelease];
      };
      IMP imp_ = imp_implementationWithBlock(block_);
      return obj<value<IMP>>(imp_);
  ")

(def *yes* (fnumber->native-int 1))
(def *no* (fnumber->native-int 0))


;;; Helper functions


(defn defclass [class-name superclass-name]
  (doto (-> (objc-get-class superclass-name)
            (objc-allocate-class-pair class-name))
    (objc-register-class-pair)))

(defn defmethod [cls selector-name types lambda]
  (class-add-method cls
                    selector-name
                    (imp-implementation-with-ferret-lambda lambda
                                                           (- (count types) 1))
                    types))

(def send objc-msg-send)

(def send-super objc-msg-send-super)

(def class-of objc-get-class)

(def int fnumber->native-int)

(def str fstr->cfstr)

(defmacro autoreleasepool [& body]
  `(let [pool (-> (objc-get-class "NSAutoreleasePool")
                  (objc-msg-send "alloc")
                  (objc-msg-send "init"))]
     ~@body
     (objc-msg-send pool "drain")
     (objc-msg-send pool "release")))
