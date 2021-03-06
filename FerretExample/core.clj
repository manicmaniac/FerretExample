(require 'objc 'uikit)

(doto (objc/defclass "ViewController" "UIViewController")
  (objc/defmethod "viewDidLoad" "@:"
    (fn [self]
      (objc/send-super self "viewDidLoad")
      (doto (objc/send self "view")
        (objc/send "setBackgroundColor:" (-> (objc/class-of "UIColor")
                                             (objc/send "systemGrayColor")))
        (objc/send "addSubview:" (doto (-> (objc/class-of "UIImageView")
                                           (objc/send "alloc")
                                           (objc/send "initWithImage:" (-> (objc/class-of "UIImage")
                                                                           (objc/send "imageNamed:" (objc/str "ferret.png"))))
                                           (objc/send "autorelease"))
                                   (objc/send "setAutoresizingMask:" uikit/*flexible-width-and-height*)
                                   (objc/send "setContentMode:" uikit/*scale-aspect-fit*)))))))

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

(objc/autoreleasepool
  (uikit/uiapplication-main *command-line-args* "AppDelegate"))
