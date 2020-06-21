(require 'objc 'uikit)

(doto (-> (objc/objc-get-class "UIViewController")
          (objc/objc-allocate-class-pair "ViewController"))
  (objc/objc-register-class-pair)
  (objc/class-add-method "viewDidLoad"
                         (objc/imp-implementation-with-ferret-lambda
                           (fn [self]
                             (let [view (objc/objc-msg-send self "view")]
                               (let [color (-> (objc/objc-get-class "UIColor")
                                               (objc/objc-msg-send "systemGrayColor"))]
                                 (objc/objc-msg-send view "setBackgroundColor:" color))
                               (let [image (-> (objc/objc-get-class "UIImage")
                                               (objc/objc-msg-send "imageNamed:" (objc/fstr->cfstr "ferret.png")))
                                     imageView (doto (-> (objc/objc-get-class "UIImageView")
                                                         (objc/objc-msg-send "alloc")
                                                         (objc/objc-msg-send "initWithImage:" image))
                                                 (objc/objc-msg-send "setAutoresizingMask:" (objc/fnumber->native-int (bit-or (bit-shift-left 1 1)
                                                                                                                              (bit-shift-left 1 4))))
                                                 (objc/objc-msg-send "setContentMode:" (objc/fnumber->native-int 1)))]
                                 (objc/objc-msg-send view "addSubview:" imageView))))
                           1)
                         "@:"))

(doto (-> (objc/objc-get-class "UIResponder")
          (objc/objc-allocate-class-pair "AppDelegate"))
  (objc/objc-register-class-pair)
  (objc/class-add-method "application:didFinishLaunchingWithOptions:" 
                         (objc/imp-implementation-with-ferret-lambda
                           (fn [self application options]
                             (let [window (-> (objc/objc-get-class "UIWindow")
                                              (objc/objc-msg-send "alloc")
                                              (objc/objc-msg-send "init"))]
                               (let [viewController (-> (objc/objc-get-class "ViewController")
                                                        (objc/objc-msg-send "alloc")
                                                        (objc/objc-msg-send "init"))]
                                 (objc/objc-msg-send window "setRootViewController:" viewController))
                               (objc/objc-msg-send window "makeKeyAndVisible"))
                             objc/*yes*)
                           3)
                         "@:@@"))

(uikit/uiapplication-main *command-line-args* "AppDelegate")
