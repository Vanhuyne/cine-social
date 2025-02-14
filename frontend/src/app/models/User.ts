export interface UserProfile {
    userId?: number;
    username: string;
    email: string;
    profilePicture?: string;
  }
  
  export interface UpdateProfileResponse {
    message: string;
    accessToken: string;
    refreshToken: string;
  }