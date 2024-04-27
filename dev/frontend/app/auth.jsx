import { useRouter } from "next/navigation";
import { useEffect } from 'react';

export const useAuth = () => {
  
  const router = useRouter();

  useEffect(() => {

    const isLoggedIn = localStorage.getItem('login');
    if (!isLoggedIn) {
      // Redirect to login page if not logged in
      router.push('/');
    }
  }, []);

  return;
};