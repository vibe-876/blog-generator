;; Get it, Cam's Markup, camarkup? Hilarious, I know.
;; I didn't want to risk messing up someone's preexisting
;; markup-mode (I'm assuming that exists somewhere in the ether).



(setq camarkup-header-file "header.html"
      camarkup-body-file "body.html"
      camarkup-jar "target/uberjar/blog-generator-0.1.0-SNAPSHOT-standalone.jar")

(defun camarkup-insert-link ()
  "Insert a camarkup style link at the
current position of the cursor.

This should only be called interactively."
  (interactive)
  (let ((address (read-string "U.R.I.: "))
	(text (read-string "Text to display: ")))
    (insert (concat "!" text "@" address "\n"))))

(defun camarkup-build-page ()
  "Builds a webpage using https://github.com/vibe-876/blog-generator.
This should not be used for anything else, or by anyone else, since it
relies on for specific non-public files I've written."
  (interactive)
  (if (and camarkup-header-file camarkup-body-file)
      (let ((page-name (file-name-base (buffer-name))))
	
	(shell-command (concat "java -jar "
			       (if camarkup-jar camarkup-jar "/usr/local/bin/blogg.jar")
			       " " (buffer-name) " "
			       page-name ".html "
			       camarkup-header-file " " camarkup-body-file))
	(message "Done :3 ."))
    
    (message "Error: are camarkup-header-file and camarkup-body-file both defined?")))


(defvar-keymap camarkup-mode-map
  :doc "A keymap for `camarkup-mode'."
  :parent text-mode-map
  "C-c i" #'camarkup-insert-link
  "C-c C-c" #'camarkup-build-page)

(defcustom camarkup-mode-hook nil
  "Hook for `camarkup-mode'."
  :type 'hook)

(define-derived-mode camarkup-mode text-mode "camarkup"
  "Major mode for editing a specific kinda markup.
\\{camarkup-mode-map}
See https://github.com/vibe-876/blog-generator for
more details."
  (use-local-map camarkup-mode-map))

(add-to-list 'auto-mode-alist '("\\.cmu$" . camarkup-mode))
