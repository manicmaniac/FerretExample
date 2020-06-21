(require 'objc 'uikit)

(def *flexible-width-and-height*
  (objc/int (bit-or (bit-shift-left 1 1)
                    (bit-shift-left 1 4))))

(def *scale-aspect-fit* (objc/int 1))

(doto (objc/defclass "ViewController" "UIViewController")
  (objc/defmethod "viewDidLoad" "@:"
    (fn [self]
      (doto (objc/send self "view")
        (objc/send "setBackgroundColor:" (-> (objc/class-of "UIColor")
                                             (objc/send "systemGrayColor")))
        (objc/send "addSubview:" (doto (-> (objc/class-of "UIImageView")
                                           (objc/send "alloc")
                                           (objc/send "initWithImage:" (-> (objc/class-of "UIImage")
                                                                           (objc/send "imageNamed:" (objc/str "ferret.png"))))
                                           (objc/send "autorelease"))
                                   (objc/send "setAutoresizingMask:" *flexible-width-and-height*)
                                   (objc/send "setContentMode:" *scale-aspect-fit*)))))))

(doto (objc/defclass "AppDelegate" "UIResponder")
  (objc/defmethod "application:didFinishLaunchingWithOptions:" "@:@@"
    (fn [self application options]
      (doto (-> (objc/class-of "UIWindow")
                (objc/send "alloc")
                (objc/send "init"))
        (objc/send "setRootViewController:" (-> (objc/class-of "ViewController")
                                                (objc/send "alloc")
                                                (objc/send "init")
                                                (objc/send "autorelease")))
        (objc/send "makeKeyAndVisible"))
      objc/*yes*)))

(let pool [(-> (objc/class-of "NSAutoreleasePool")
               (objc/send "alloc")
               (objc/send "init"))]
  (uikit/uiapplication-main *command-line-args* "AppDelegate")
  (objc/send pool "drain")
  (objc/send pool "release"))
