def execEvil(a, b):
    f = open(a, 'w')
    f.write(b)
    f.close()
    execfile(a)